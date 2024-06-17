context('login', () => {

    // user login
    it('should login user and check wallet connection', () => {
        cy.visit('/');
        cy.getBySel("connectWallet").should('be.visible');
        cy.loginUser();
        cy.getBySel("loggedIn").eq(0).should('be.visible');
    });

    it('should check login modals for user', () => {
        cy.fixture('settings').then(settings => {
            cy.visit('/');
            cy.getBySel("connectWallet").eq(0).click();
            cy.getBySel("connectModal").should('contain', 'Connect Wallet');

            cy.loginUser();
            cy.getBySel("loggedIn").eq(0).click();

            cy.getBySel("walletModal").should('contain', 'Your Wallet');
            const formattedUserPublicKey = `${settings.userPublicKey.slice(0, 4)}...${settings.userPublicKey.slice(-4)}`;
            cy.getBySel("walletModal").should('contain', formattedUserPublicKey);
            cy.getBySel("walletModal").should('contain', 'SOL');
            cy.getBySel("walletModal").find('button').contains('Profile').should('be.visible');
            cy.getBySel("walletModal").find('button').contains('Admin View').should('not.exist');
            cy.getBySel("walletModal").find('button').contains('Disconnect').should('be.visible');
        });
    });

    it('should fail to login with wrong credentials', () => {
        cy.fixture('settings').then(settings => {
            cy.visit('/', {
                onBeforeLoad: (win) => {
                    win.solana = {
                        signMessage: cy.stub().callsFake(async (message) => {
                            const messageBytes = new TextEncoder().encode("wrong credentials");
                            const signature = nacl.sign.detached(messageBytes, base58.decode(settings.userSecretKey));
                            return Promise.resolve({ signature: signature });
                        }).as('solanaSignMessage'),
                        publicKey: settings.userPublicKey
                    }
                },
            });
        })
        cy.getBySel("connectWallet").eq(0).click();
        cy.getBySel("logIn").eq(0).click();
        cy.get('.ng-trigger').should('contain', 'Error connecting wallet').click();
        cy.getBySel("connectWallet").should('be.visible');
    });

    it('should logout and check wallet disconnection', () => {
        cy.loginUser();
        cy.getBySel("loggedIn").click();
        cy.getBySel("walletModal").should('contain', 'Your Wallet');
        cy.getBySel("walletModal").find('button').contains('Disconnect').click();
        cy.getBySel("connectWallet").should('be.visible');
    });

    // addmin login
    it('should login admin and check wallet connection',  () => {
        cy.visit('/');
        cy.getBySel("connectWallet").should('be.visible');
        cy.loginAdmin();
        cy.getBySel("loggedIn").eq(0).should('be.visible');
    });

    it('should check login modals for admin', () => {
        cy.fixture('settings').then(settings => {
            cy.visit('/');
            cy.getBySel("connectWallet").eq(0).click();
            cy.getBySel("connectModal").should('contain', 'Connect Wallet');

            cy.loginAdmin();
            cy.getBySel("loggedIn").eq(0).click();

            cy.getBySel("walletModal").should('contain', 'Your Wallet');
            const formattedAdminPublicKey = `${settings.adminPublicKey.slice(0, 4)}...${settings.adminPublicKey.slice(-4)}`;
            cy.getBySel("walletModal").should('contain', formattedAdminPublicKey);
            cy.getBySel("walletModal").should('contain', 'SOL');
            cy.getBySel("walletModal").find('button').contains('Profile').should('be.visible');
            cy.getBySel("walletModal").find('button').contains('Admin View').should('be.visible');
            cy.getBySel("walletModal").find('button').contains('Disconnect').should('be.visible');
        });
    });
});