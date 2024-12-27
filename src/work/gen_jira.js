// ==UserScript==
// @name         工时统计助手
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  统计confluence页面中的工时信息，按SP列统计工时，按执行人分组
// @author       Your name
// @match        http://confluence.shinho.net.cn/pages/viewpage.action*
// @grant        none
// ==/UserScript==

(function() {
    'use strict';

    // 创建统计按钮
    function createStatsButton() {
        const button = document.createElement('button');
        button.textContent = '统计工时';
        button.style.cssText = 'position: fixed; top: 10px; right: 10px; z-index: 9999; padding: 8px 16px; background: #0052cc; color: white; border: none; border-radius: 3px; cursor: pointer;';
        button.onclick = calculateStats;
        document.body.appendChild(button);
    }

    // 创建结果显示面板
    function createResultPanel() {
        const panel = document.createElement('div');
        panel.id = 'workHoursPanel';
        panel.style.cssText = `
            position: fixed;
            top: 50px;
            right: 10px;
            width: 1000px;
            background: white;
            border: 1px solid #ccc;
            border-radius: 4px;
            padding: 15px;
            display: none;
            z-index: 9999;
            box-shadow: 0 2px 4px rgba(0,0,0,0.2);
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, "Fira Sans", "Droid Sans", "Helvetica Neue", sans-serif;
            transition: all 0.3s ease;
        `;
        document.body.appendChild(panel);
        return panel;
    }

    // 从用户链接中提取工号和姓名
    function extractUserInfo(userLink) {
        if (!userLink) {
            console.log('未找到用户链接');
            return null;
        }

        const userName = userLink.textContent.trim();
        const userId = userLink.getAttribute('data-username');
        console.log(`提取用户信息: 姓名=${userName}, 工号=${userId}`);

        return {
            id: userId,
            name: userName
        };
    }

    // 复制用户任务数据
    function copyUserTasks(button, tasks) {
        // 生成Excel格式的文本
        const headers = ['子任务', '编号', '故事', '姓名', '工号', 'SP'];
        const rows = tasks.map(task => [
            '【前端】' + task.subtask,
            task.number,
            task.story,
            task.userName,
            task.userId,
            task.sp
        ]);
        const excelText = rows
            .map(row => row.join('\t'))
            .join('\n');


        // 创建一个临时文本区域
        const textArea = document.createElement('textarea');
        textArea.value = excelText;
        document.body.appendChild(textArea);
        textArea.style.position = 'fixed';
        textArea.style.left = '-999999px';
        textArea.style.top = '-999999px';
        textArea.select();

        try {
            // 尝试使用传统的复制方法
            document.execCommand('copy');
            button.textContent = '已复制';
            button.style.background = '#00875a';
            setTimeout(() => {
                button.textContent = '复制';
                button.style.background = '#0052cc';
            }, 1000);
        } catch (err) {
            console.error('复制失败:', err);
            alert('复制失败，请手动复制');
        } finally {
            // 清理临时文本区域
            document.body.removeChild(textArea);
        }
    }

    // 计算统计信息
    function calculateStats() {
        console.log('开始计算统计信息...');
        const tables = document.querySelectorAll('table.confluenceTable');
        console.log(`找到 ${tables.length} 个表格`);
        
        let targetTable = null;
        let spColumnIndex = -1;
        let executorIndex = -1;

        // 遍历所有表格找到目标表格
        for (let table of tables) {
            const rows = table.querySelectorAll('tr');
            console.log(`当前表格有 ${rows.length} 行`);
            
            // 获取第一行(表头)
            const headerRow = rows[0];
            if (!headerRow) continue;
            
            const headers = headerRow.querySelectorAll('th');
            console.log(`表头有 ${headers.length} 列`);
            
            // 检查是否是6列的表格
            if (headers.length === 6) {
                // 获取表头文本
                const headerTexts = Array.from(headers).map(th => th.textContent.trim());
                console.log('表头内容:', headerTexts);
                
                // 检查是否包含关键列名
                if (headerTexts[0].includes('任务名称') && headerTexts[headerTexts.length - 1].includes('负责人')) {
                    targetTable = table;
                    console.log('找到目标表格: 6列且包含正确的表头');
                    // 设置列索引
                    spColumnIndex = headerTexts.findIndex(text => text.includes('SP') || text.includes('工时') || text.includes('点数'));
                    executorIndex = headerTexts.length - 1; // 最后一列是负责人
                    console.log(`SP列索引: ${spColumnIndex}, 负责人列索引: ${executorIndex}`);
                    break;
                }
            }
        }

        if (!targetTable) {
            console.error('未找到符合要求的表格(6列且包含正确的表头)');
            alert('未找到符合要求的表格，请检查页面结构');
            return;
        }

        console.log('开始解析任务数据...');
        const tasks = [];
        const rows = targetTable.querySelectorAll('tr');
        let currentStory = null;
        let currentStoryName = null;
        let currentStoryExecutors = null; // 存储当前故事的负责人信息

        // 从第二行开始遍历（跳过表头）
        for (let i = 1; i < rows.length; i++) {
            const row = rows[i];
            const cells = row.querySelectorAll('td');
            if (cells.length <= Math.max(spColumnIndex, executorIndex)) {
                console.log(`第 ${i} 行的列数不足,跳过`);
                continue;
            }

            const firstCell = cells[0];
            const firstCellText = firstCell.textContent.trim();
            console.log(`处理第 ${i} 行,首列内容: ${firstCellText}`);

            // 检查是否是故事行
            if (firstCellText.includes('A2-')) {
                const parts = firstCellText.split('-').map(p => p.trim());
                currentStory = parts[0] + '-' + parts[1];
                currentStoryName = parts.slice(2).join('-').trim().replace('待办', '');
                
                // 获取故事行的负责人信息
                const storyExecutorCell = cells[executorIndex];
                currentStoryExecutors = [];
                const storyUserLinks = storyExecutorCell.querySelectorAll('a.confluence-userlink');
                storyUserLinks.forEach(link => {
                    const userInfo = extractUserInfo(link);
                    if (userInfo) {
                        currentStoryExecutors.push(userInfo);
                    }
                });
                
                console.log(`发现故事行: ${currentStory} - ${currentStoryName}`);
                console.log('故事负责人:', currentStoryExecutors);
                continue;
            }

            const spCell = cells[spColumnIndex];
            const executorCell = cells[executorIndex];
            
            if (!spCell || !executorCell) {
                console.log(`第 ${i} 行缺少SP列或执行人列,跳过`);
                continue;
            }

            const spText = spCell.textContent.trim();
            const sp = parseFloat(spText) || 0;
            console.log(`SP值: ${sp}`);

            // 获取子任务的负责人信息
            let taskExecutors = [];
            const taskUserLinks = executorCell.querySelectorAll('a.confluence-userlink');
            
            // 如果子任务行有负责人，使用子任务的负责人
            if (taskUserLinks.length > 0) {
                taskUserLinks.forEach(link => {
                    const userInfo = extractUserInfo(link);
                    if (userInfo) {
                        taskExecutors.push(userInfo);
                    }
                });
            } 
            // 如果子任务行没有负责人，使用故事的负责人
            else if (currentStoryExecutors && currentStoryExecutors.length > 0) {
                taskExecutors = currentStoryExecutors;
            }

            console.log(`任务负责人:`, taskExecutors);

            // 为每个负责人创建一条任务记录
            if (taskExecutors.length > 0 && sp > 0) {
                taskExecutors.forEach(executor => {
                    tasks.push({
                        subtask: firstCellText,
                        number: currentStory || '',
                        story: currentStoryName || '未分类',
                        userName: executor.name,
                        userId: executor.id,
                        sp: sp  // 不再除以负责人数量，每个人都获得完整的SP值
                    });
                    console.log(`添加任务: ${firstCellText} - 负责人: ${executor.name} - SP: ${sp}`);
                });
            }
        }

        console.log(`共解析出 ${tasks.length} 个任务`);
        console.log('开始显示结果...');
        displayResults(tasks);
    }

    // 显示结果
    function displayResults(tasks) {
        const panel = document.getElementById('workHoursPanel') || createResultPanel();
        panel.style.display = 'block';

        // 添加折叠/展开按钮和标题栏
        let html = `
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px;">
                <h3 style="margin: 0; color: #172B4D;">工时统计结果</h3>
                <div style="display: flex; gap: 10px;">
                    <button id="togglePanel" style="
                        padding: 6px 12px;
                        background: #0052cc;
                        color: white;
                        border: none;
                        border-radius: 3px;
                        cursor: pointer;
                    ">折叠</button>
                    <button onclick="this.parentElement.parentElement.parentElement.style.display='none'" style="
                        padding: 6px 12px;
                        background: #0052cc;
                        color: white;
                        border: none;
                        border-radius: 3px;
                        cursor: pointer;
                    ">关闭</button>
                </div>
            </div>
            <div id="panelContent">
        `;

        // 总计
        const totalSP = tasks.reduce((sum, task) => sum + task.sp, 0);
        html += `<p style="font-size: 16px;"><strong>总SP：${totalSP.toFixed(1)}</strong></p>`;

        // 按人员统计
        const userStats = tasks.reduce((stats, task) => {
            const key = `${task.userName}(${task.userId})`;
            if (!stats[key]) {
                stats[key] = {
                    sp: 0,
                    tasks: [],
                    userId: task.userId
                };
            }
            stats[key].sp += task.sp;
            stats[key].tasks.push(task);
            return stats;
        }, {});

        // 显示人员统计
        html += `
            <div style="margin: 15px 0;">
                <h4 style="margin: 0 0 10px 0; color: #172B4D;">人员工时统计：</h4>
                <div style="display: flex; flex-wrap: wrap; gap: 10px;">
                    ${Object.entries(userStats)
            .sort((a, b) => b[1].sp - a[1].sp)
            .map(([user, data], index) => `
                            <div style="
                                background-color: #f4f5f7;
                                padding: 8px 12px;
                                border-radius: 3px;
                                border: 1px solid #dfe1e6;
                                display: flex;
                                align-items: center;
                                gap: 10px;
                            ">
                                <span><strong>${user}：</strong>${data.sp.toFixed(1)} SP</span>
                                <button
                                    class="copy-button"
                                    data-tasks='${JSON.stringify(data.tasks)}'
                                    style="
                                        padding: 4px 8px;
                                        background: #0052cc;
                                        color: white;
                                        border: none;
                                        border-radius: 3px;
                                        cursor: pointer;
                                        font-size: 12px;
                                    "
                                >
                                    复制
                                </button>
                            </div>
                        `).join('')}
                </div>
            </div>
            <hr style="border: 0; border-top: 1px solid #dfe1e6; margin: 15px 0;">
        `;

        // 列表视图
        html += `<h4 style="margin: 15px 0 10px 0; color: #172B4D;">任务明细：</h4>`;
        html += `<div style="display: flex; flex-direction: column; gap: 10px;">`;

        // 按故事分组显示任务
        const tasksByStory = tasks.reduce((acc, task) => {
            if (!acc[task.story]) {
                acc[task.story] = [];
            }
            acc[task.story].push(task);
            return acc;
        }, {});

        Object.entries(tasksByStory).forEach(([story, storyTasks]) => {
            const storyTotalSP = storyTasks.reduce((sum, task) => sum + task.sp, 0);

            html += `
                <div style="
                    background-color: #f4f5f7;
                    border: 1px solid #dfe1e6;
                    border-radius: 3px;
                    margin-bottom: 10px;
                ">
                    <div style="
                        padding: 8px 12px;
                        background-color: #e9eaed;
                        border-bottom: 1px solid #dfe1e6;
                        font-weight: bold;
                    ">
                        ${story} (总SP: ${storyTotalSP.toFixed(1)})
                    </div>
                    <div style="padding: 8px 12px;">
                        ${storyTasks.map(task => `
                            <div style="
                                padding: 8px;
                                margin: 4px 0;
                                background-color: white;
                                border: 1px solid #dfe1e6;
                                border-radius: 3px;
                            ">
                                <div style="margin-bottom: 4px;">
                                    <span style="color: #5e6c84;">任务：</span>
                                    <span style="font-weight: 500;">【后端】${task.subtask}</span>
                                </div>
                                <div style="
                                    display: grid;
                                    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
                                    gap: 8px;
                                    font-size: 12px;
                                    color: #5e6c84;
                                ">
                                    <div>编号：${task.number}</div>
                                    <div>执行人：${task.userName}</div>
                                    <div>工号：${task.userId}</div>
                                    <div>SP：${task.sp}</div>
                                </div>
                            </div>
                        `).join('')}
                    </div>
                </div>
            `;
        });

        html += `</div>`;

        // 在最后添加结束div
        html += '</div>';

        panel.innerHTML = html;

        // 添加折叠/展开功能
        const toggleBtn = panel.querySelector('#togglePanel');
        const content = panel.querySelector('#panelContent');
        let isCollapsed = false;

        toggleBtn.addEventListener('click', () => {
            if (isCollapsed) {
                // 展开
                content.style.display = 'block';
                panel.style.maxHeight = '80vh';
                toggleBtn.textContent = '折叠';
                panel.style.width = '1000px';
            } else {
                // 折叠
                content.style.display = 'none';
                panel.style.maxHeight = 'auto';
                toggleBtn.textContent = '展开';
                panel.style.width = '200px';
            }
            isCollapsed = !isCollapsed;
        });

        // 添加复制按钮的事件监听器
        panel.querySelectorAll('.copy-button').forEach(button => {
            button.addEventListener('click', function() {
                const tasksData = JSON.parse(this.getAttribute('data-tasks'));
                copyUserTasks(this, tasksData);
            });
        });
    }

    // 修改 parseTable 函数中的解析部分
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
        let storyUserNumber = null;

        // 获取表格所有行
        const rows = document.querySelectorAll('.confluenceTable tr');
        
        // 跳过表头
        for (let i = 1; i < rows.length; i++) {
            const row = rows[i];
            const cells = row.querySelectorAll('td');
            if (cells.length === 0) continue;

            const firstCell = cells[0];
            let firstValue = firstCell.textContent.trim();

            // 检查是否是故事行（通常背景色为灰色且包含故事编号）
            if (firstCell.classList.contains('confluenceTd') && firstValue.includes('A2-')) {
                firstValue = firstValue
                    .replace(/[\r\n]+/g, ' ')  // 将所有换行和回车替换为空格
                    .replace(/\s+/g, ' ')      // 将多个连续空格替换为单个空格
                    .trim();

                story = firstValue.split(' - ')[0];
                storyName = firstValue.split(' - ')[1];

                // 获取故事负责人信息
                const lastCell = cells[cells.length - 1];
                if (lastCell) {
                    const userLink = lastCell.querySelector('a.confluence-userlink');
                    if (userLink) {
                        storyUser = userLink.textContent.trim();
                        storyUserNumber = userLink.getAttribute('data-username');
                    }
                }
                continue;
            }

            if (!firstValue) continue;

            const taskName = firstValue;
            const spStr = cells[cells.length - 2]?.textContent.trim() || '0';
            const sp = parseInt(spStr);
            if (isNaN(sp)) continue;

            // 获取任务执行人信息
            let taskUser = '';
            let taskUserNumber = '';
            const lastCell = cells[cells.length - 1];
            if (lastCell) {
                const userLink = lastCell.querySelector('a.confluence-userlink');
                if (userLink) {
                    taskUser = userLink.textContent.trim();
                    taskUserNumber = userLink.getAttribute('data-username');
                }
            }

            const personStr = taskUser || storyUser;
            const personNumberStr = taskUserNumber || storyUserNumber;

            if (!personStr) continue;

            const persons = personStr.split(/[\s,]+/);
            const personNumbers = personNumberStr ? [personNumberStr] : [];

            for (let i = 0; i < persons.length; i++) {
                const person = persons[i].trim();
                if (!person) continue;
                taskList.push({
                    story,
                    storyName,
                    taskName,
                    person,
                    personNumber: personNumbers[i] || '',
                    sp
                });
            }
        }

        return taskList;
    }

    // 初始化
    createStatsButton();
})();