document.addEventListener('DOMContentLoaded', function() {
    // Toggle submenus when action buttons are clicked
    document.getElementById('build-btn').addEventListener('click', function() {
        const submenu = document.getElementById('build-submenu');
        const trainSubmenu = document.getElementById('train-submenu');
        
        if (submenu.style.display === 'block') {
            submenu.style.display = 'none';
        } else {
            submenu.style.display = 'block';
            trainSubmenu.style.display = 'none';
        }
    });
    
    document.getElementById('train-btn').addEventListener('click', function() {
        const submenu = document.getElementById('train-submenu');
        const buildSubmenu = document.getElementById('build-submenu');
        
        if (submenu.style.display === 'block') {
            submenu.style.display = 'none';
        } else {
            submenu.style.display = 'block';
            buildSubmenu.style.display = 'none';
        }
    });
    
    // Add a log entry
    function addLogEntry(text) {
        const log = document.getElementById('game-log');
        const entry = document.createElement('div');
        entry.className = 'log-entry';
        entry.textContent = text;
        log.appendChild(entry);
        log.scrollTop = log.scrollHeight;
    }
    
    // End turn button
    document.getElementById('end-turn-btn').addEventListener('click', function() {
        // This would normally trigger the next player's turn in the actual game
        addLogEntry('Turn ended. Next player\'s turn.');
    });
    
    // Building buttons
    document.getElementById('monument-btn').addEventListener('click', function() {
        // Check if player has enough resources
        const stone = parseInt(document.getElementById('stone-amount').textContent);
        const wood = parseInt(document.getElementById('wood-amount').textContent);
        
        if (stone >= 200 && wood >= 50) {
            document.getElementById('stone-amount').textContent = stone - 200;
            document.getElementById('wood-amount').textContent = wood - 50;
            addLogEntry('Monument built in selected city.');
            document.querySelector('.city-buildings').textContent = 'Buildings: Monument';
        } else {
            addLogEntry('Not enough resources to build Monument.');
        }
    });
    
    // Create a simple hexagonal grid
    function createHexGrid(rows, cols) {
        const grid = document.querySelector('.hex-grid');
        grid.innerHTML = '';
        
        const terrainTypes = ['grassland', 'forest', 'mountain'];
        
        for (let r = 0; r < rows; r++) {
            const rowElem = document.createElement('div');
            rowElem.className = 'hex-row';
            
            for (let c = 0; c < cols; c++) {
                const hex = document.createElement('div');
                // Randomly select terrain type
                const terrainType = terrainTypes[Math.floor(Math.random() * terrainTypes.length)];
                hex.className = `hexagon ${terrainType}`;
                
                hex.addEventListener('click', function() {
                    addLogEntry(`Selected ${terrainType} tile at position (${c},${r}).`);
                });
                
                rowElem.appendChild(hex);
            }
            
            grid.appendChild(rowElem);
        }
    }
    
    // Initialize a small hex grid
    createHexGrid(8, 8);
});