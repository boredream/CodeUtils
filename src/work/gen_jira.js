// ==UserScript==
// @name         Confluence任务统计工具
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  统计Confluence页面中的任务分配和工时
// @author       Your name
// @match        http://confluence.shinho.net.cn/pages/viewpage.action?pageId=*
// @grant        GM_addStyle
// ==/UserScript==

(function() {
    'use strict';

    // 添加样式
    GM_addStyle(`
        #taskPanel {
            position: fixed;
            right: 20px;
            top: 20px;
            background: white;
            border: 1px solid #ddd;
            border-radius: 4px;
            padding: 10px;
            width: 300px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
            z-index: 9999;
        }
        #taskPanel .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 10px;
            padding-bottom: 5px;
            border-bottom: 1px solid #eee;
        }
        #taskPanel .content {
            max-height: 500px;
            overflow-y: auto;
        }
        #taskPanel pre {
            margin: 0;
            white-space: pre-wrap;
            word-wrap: break-word;
        }
        #taskPanel button {
            padding: 5px 10px;
            margin: 5px;
            border: 1px solid #ddd;
            border-radius: 3px;
            background: #f5f5f5;
            cursor: pointer;
        }
        #taskPanel button:hover {
            background: #e5e5e5;
        }
        .copy-success {
            color: green;
            font-size: 12px;
            margin-left: 10px;
        }
    `);

    // 创建面板
    const panel = document.createElement('div');
    panel.id = 'taskPanel';
    panel.innerHTML = `
        <div class="header">
            <h3 style="margin:0">任务统计</h3>
            <button id="startAnalysis">开始分析</button>
        </div>
        <div class="content">
            <div id="taskResult"></div>
            <div style="margin-top:10px">
                <button id="copyDetails">复制详情</button>
                <button id="copySummary">复制统计</button>
                <span id="copyStatus"></span>
            </div>
        </div>
    `;
    document.body.appendChild(panel);

    // 解析表格数据的函数
    async function parseTable() {
        // 等待JIRA信息加载
        await new Promise((resolve) => {
            const checkInterval = setInterval(() => {
                if (!document.body.textContent.includes('正在获取问题细节')) {
                    clearInterval(checkInterval);
                    resolve();
                }
            }, 1000);
        });

        const taskList = [];
        let story = null;
        let storyName = null;
        let storyUser = null;

        // 获取表格所有行
        const rows = document.querySelectorAll('table.confluenceTable tr');

        // 跳过表头
        for (let i = 1; i < rows.length; i++) {
            const row = rows[i];
            const cells = row.querySelectorAll('td');
            if (cells.length === 0) continue;

            const firstCell = cells[0];
            const firstValue = firstCell.textContent.trim();

            if (firstCell.classList.contains('highlight-grey') && firstValue.includes('A2-')) {
                const parts = firstValue.split(' - ');
                story = parts[0].trim();
                storyName = parts.length > 1 ? parts[1].trim() : '';

                const lastCell = cells[cells.length - 1];
                if (lastCell && lastCell.textContent.trim()) {
                    storyUser = lastCell.textContent.trim();
                }
                continue;
            }

            if (!firstValue) continue;

            const taskName = firstValue;
            const spStr = cells[cells.length - 2]?.textContent.trim() || '0';
            const sp = parseInt(spStr);
            if (isNaN(sp)) continue;

            const taskUser = cells[cells.length - 1]?.textContent.trim();
            const personStr = taskUser || storyUser;

            if (!personStr) continue;

            const persons = personStr.split(/[\s,]+/);
            for (const person of persons) {
                if (!person.trim()) continue;
                taskList.push({
                    story,
                    storyName,
                    taskName,
                    person: person.trim(),
                    sp
                });
            }
        }

        return taskList;
    }

    // 生成结果文本
    function generateResults(taskList) {
        let detailsText = '';
        const userStoryMap = {};
        const userTotalSpMap = {};

        // 生成详情文本
        for (const task of taskList) {
            if (!userStoryMap[task.person]) {
                userStoryMap[task.person] = new Set();
            }
            userStoryMap[task.person].add(task.story);
            userTotalSpMap[task.person] = (userTotalSpMap[task.person] || 0) + task.sp;

            detailsText += `${task.taskName}\t${task.story}\t${task.storyName}\t${task.person}\t\t${task.sp}\t\t\t前端\n`;
        }

        // 生成统计文本
        let summaryText = '=== 统计结果 ===\n';
        for (const [name, sp] of Object.entries(userTotalSpMap)) {
            summaryText += `姓名：${name}\n`;
            summaryText += `SP：${sp}\n`;
            summaryText += `故事：${userStoryMap[name].size}\n\n`;
        }

        return { detailsText, summaryText };
    }

    // 复制到剪贴板
    function copyToClipboard(text) {
        const textarea = document.createElement('textarea');
        textarea.value = text;
        document.body.appendChild(textarea);
        textarea.select();
        document.execCommand('copy');
        document.body.removeChild(textarea);

        const status = document.getElementById('copyStatus');
        status.textContent = '复制成功!';
        setTimeout(() => status.textContent = '', 2000);
    }

    // 事件处理
    let results = null;
    document.getElementById('startAnalysis').addEventListener('click', async () => {
        const taskList = await parseTable();
        results = generateResults(taskList);

        // 显示结果
        document.getElementById('taskResult').innerHTML = `
            <pre style="font-size:12px">${results.summaryText}</pre>
        `;
    });

    document.getElementById('copyDetails').addEventListener('click', () => {
        if (results) {
            copyToClipboard(results.detailsText);
        }
    });

    document.getElementById('copySummary').addEventListener('click', () => {
        if (results) {
            copyToClipboard(results.summaryText);
        }
    });
})();