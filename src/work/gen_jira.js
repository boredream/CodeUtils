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
            max-height: 80vh;
            background: white;
            border: 1px solid #ccc;
            border-radius: 4px;
            padding: 15px;
            overflow-y: auto;
            display: none;
            z-index: 9999;
            box-shadow: 0 2px 4px rgba(0,0,0,0.2);
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, "Fira Sans", "Droid Sans", "Helvetica Neue", sans-serif;
        `;
        document.body.appendChild(panel);
        return panel;
    }

    // 从用户链接中提取工号和姓名
    function extractUserInfo(userLink) {
        if (!userLink) return null;

        const userName = userLink.textContent.trim();
        const userId = userLink.getAttribute('data-username');

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
            '【后端】' + task.subtask,
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
        const tables = document.querySelectorAll('.relative-table');
        let targetTable = null;
        let spColumnIndex = -1;
        let numberColumnIndex = -1;
        let storyColumnIndex = -1;
        let subtaskDescIndex = -1;  // 子任务描述列索引

        for (let table of tables) {
            const headerRows = table.querySelectorAll('tr');
            for (let row of headerRows) {
                const cells = row.querySelectorAll('th, td');
                const headers = Array.from(cells).map(cell => cell.textContent.trim());
                const spIndex = headers.indexOf('SP');
                if (spIndex !== -1) {
                    targetTable = table;
                    spColumnIndex = spIndex;
                    numberColumnIndex = headers.indexOf('编号');
                    storyColumnIndex = headers.indexOf('故事');
                    subtaskDescIndex = headers.indexOf('子任务描述');  // 获取子任务描述列的索引
                    break;
                }
            }
            if (targetTable) break;
        }

        if (!targetTable) {
            alert('未找到包含SP列的表格，请检查页面结构');
            return;
        }

        const tasks = [];
        const rows = targetTable.querySelectorAll('tbody tr');
        const headerRow = targetTable.querySelector('tr');
        const headers = Array.from(headerRow.querySelectorAll('th, td')).map(cell => cell.textContent.trim());
        const executorIndex = headers.indexOf('执行人');

        if (executorIndex === -1) {
            alert('未找到执行人列，请检查表格结构');
            return;
        }

        // 收集所有任务数据
        rows.forEach(row => {
            const cells = row.querySelectorAll('td');
            if (cells.length > Math.max(spColumnIndex, executorIndex)) {
                const spCell = cells[spColumnIndex];
                const executorCell = cells[executorIndex];
                const numberCell = numberColumnIndex !== -1 ? cells[numberColumnIndex] : null;
                const storyCell = storyColumnIndex !== -1 ? cells[storyColumnIndex] : null;
                const subtaskDescCell = subtaskDescIndex !== -1 ? cells[subtaskDescIndex] : null;

                const userLink = executorCell.querySelector('.confluence-userlink');
                const userInfo = extractUserInfo(userLink);

                if (userInfo && spCell.textContent.trim()) {
                    const sp = parseFloat(spCell.textContent) || 0;
                    if (sp > 0) {
                        tasks.push({
                            subtask: subtaskDescCell ? subtaskDescCell.textContent.trim() : '',
                            number: numberCell ? numberCell.textContent.trim() : '',
                            story: storyCell ? storyCell.textContent.trim() : '未分类',
                            userName: userInfo.name,
                            userId: userInfo.id,
                            sp: sp
                        });
                    }
                }
            }
        });

        // 按故事和工号排序
        tasks.sort((a, b) => {
            const storyCompare = a.story.localeCompare(b.story);
            if (storyCompare !== 0) return storyCompare;
            return a.userId.localeCompare(b.userId);
        });

        displayResults(tasks);
    }

    // 显示结果
    function displayResults(tasks) {
        const panel = document.getElementById('workHoursPanel') || createResultPanel();
        panel.style.display = 'block';

        let html = '<h3 style="margin-top: 0; color: #172B4D;">工时统计结果</h3>';

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

        // 添加关闭按钮
        html += `
            <button onclick="this.parentElement.style.display='none'"
                    style="margin-top: 10px; padding: 6px 12px; background: #0052cc; color: white;
                           border: none; border-radius: 3px; cursor: pointer;">
                关闭
            </button>`;

        panel.innerHTML = html;

        // 添加复制按钮的事件监听器
        panel.querySelectorAll('.copy-button').forEach(button => {
            button.addEventListener('click', function() {
                const tasksData = JSON.parse(this.getAttribute('data-tasks'));
                copyUserTasks(this, tasksData);
            });
        });
    }

    // 初始化
    createStatsButton();
})();