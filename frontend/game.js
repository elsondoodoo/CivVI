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
    
    // --- Seeded Random Number Generator ---
    function mulberry32(seed) {
        return function() {
            let t = seed += 0x6D2B79F5;
            t = Math.imul(t ^ t >>> 15, t | 1);
            t ^= t + Math.imul(t ^ t >>> 7, t | 61);
            return ((t ^ t >>> 14) >>> 0) / 4294967296;
        }
    }

    // --- Random Hex Grid Generation with Seed ---
    function createHexGrid(rows, cols, seed = null) {
        const grid = document.querySelector('.hex-grid');
        grid.innerHTML = '';

        const terrainTypes = ['grassland', 'forest', 'mountain'];
        let rand = Math.random;
        let usedSeed = seed;

        // If no seed provided, generate one and update the input box
        if (seed === null || seed === "" || isNaN(seed)) {
            // Generate a random 32-bit integer seed
            usedSeed = Math.floor(Math.random() * 0xFFFFFFFF);
            // Update the input box to show the used seed
            const input = document.getElementById('seed-input');
            if (input) input.value = usedSeed;
        }
        rand = mulberry32(Number(usedSeed));

        for (let r = 0; r < rows; r++) {
            const rowElem = document.createElement('div');
            rowElem.className = 'hex-row';

            // Offset even rows for hex grid effect
            if (r % 2 === 1) rowElem.style.marginLeft = '52px';

            for (let c = 0; c < cols; c++) {
                const hex = document.createElement('div');
                // Randomly select terrain type using seeded RNG
                const terrainType = terrainTypes[Math.floor(rand() * terrainTypes.length)];
                hex.className = `hexagon ${terrainType}`;
                rowElem.appendChild(hex);
            }
            grid.appendChild(rowElem);
        }
    }

    // --- Add Seed Input UI ---
    function addSeedInput() {
        const topHud = document.querySelector('.top-hud');
        if (!topHud) return;

        const seedDiv = document.createElement('div');
        seedDiv.style.display = 'flex';
        seedDiv.style.alignItems = 'center';
        seedDiv.style.gap = '6px';
        seedDiv.style.marginLeft = '20px';

        const label = document.createElement('label');
        label.textContent = 'Map Seed:';
        label.style.fontSize = '14px';
        label.style.color = '#eee';

        const input = document.createElement('input');
        input.type = 'number';
        input.id = 'seed-input';
        input.style.width = '90px';
        input.style.marginLeft = '4px';
        input.style.padding = '2px 4px';
        input.style.borderRadius = '3px';
        input.style.border = '1px solid #555';
        input.style.background = '#222';
        input.style.color = '#eee';

        const button = document.createElement('button');
        button.textContent = 'Generate';
        button.style.marginLeft = '4px';
        button.style.padding = '2px 8px';
        button.style.borderRadius = '3px';
        button.style.border = 'none';
        button.style.background = '#4a7';
        button.style.color = '#fff';
        button.style.cursor = 'pointer';

        seedDiv.appendChild(label);
        seedDiv.appendChild(input);
        seedDiv.appendChild(button);

        topHud.appendChild(seedDiv);

        button.addEventListener('click', function() {
            const seed = input.value;
            createHexGrid(10, 10, seed);
        });
    }

    addSeedInput();
    // When first loading, create grid and display the seed used
    createHexGrid(10, 10);
});