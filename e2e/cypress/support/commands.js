import base58 from "bs58";
import nacl from "tweetnacl";

Cypress.Commands.add("loginAdmin",
    (path, visitOptions) => {
        cy.fixture('settings').then(settings => {
            cy.visit('/', {
                onBeforeLoad: (win) => {
                    win.solana = {
                        signMessage: cy.stub().callsFake(async (message) => {
                            var string = new TextDecoder().decode(message);
                            console.log("faking sign for", string);
                            const messageBytes = new TextEncoder().encode(string)
                            const signature = nacl.sign.detached(messageBytes, base58.decode(settings.adminSecretKey));
                            return Promise.resolve({ signature: signature });
                        }).as('solanaSignMessage'),
                        disconnect: cy.stub().returns(Promise.resolve('mocked_signature')).as('solanaDisConnect'),
                        connect: cy.stub().returns(Promise.resolve('mocked_signature')).as('solanaConnect'),
                        publicKey: settings.adminPublicKey,
                    }
                },
            });
        })
        cy.getBySel("connectWallet").eq(0).click();
        cy.getBySel("logIn").eq(0).should('be.visible');
        cy.getBySel("logIn").eq(0).click();
        cy.url().should('contain', '/');
        cy.get('.ng-trigger').should('contain', 'Login successful').click();
        cy.getBySel("loggedIn").eq(0).should('be.visible');
    },
);

Cypress.Commands.add("loginUser",
    (path, visitOptions) => {
        cy.fixture('settings').then(settings => {
            cy.visit('/', {
                onBeforeLoad: (win) => {
                    win.solana = {
                        signMessage: cy.stub().callsFake(async (message) => {
                            var string = new TextDecoder().decode(message);
                            console.log("faking sign for", string);
                            const messageBytes = new TextEncoder().encode(string)
                            //user sec key
                            const signature = nacl.sign.detached(messageBytes, base58.decode(settings.userSecretKey));
                            return Promise.resolve({ signature: signature });
                        }).as('solanaSignMessage'),
                        disconnect: cy.stub().returns(Promise.resolve('mocked_signature')).as('solanaDisConnect'),
                        connect: cy.stub().returns(Promise.resolve('mocked_signature')).as('solanaConnect'),
                        //user pub key
                        publicKey: settings.userPublicKey
                    }
                },
            });
        })
        cy.getBySel("connectWallet").eq(0).click();
        cy.getBySel("logIn").eq(0).should('be.visible');
        cy.getBySel("logIn").eq(0).click();
        cy.url().should('contain', '/');
        cy.get('.ng-trigger').should('contain', 'Login successful').click();
        cy.getBySel("loggedIn").eq(0).should('be.visible');
    },
);

/**
 getBySel yields elements with a data-test attribute that match a specified selector.
 */
Cypress.Commands.add('getBySel', (selector, ...args) => {
    return cy.get(`[data-test=${selector}]`, ...args)
})
/**
 getBySelLike yields elements with a data-test attribute that contains a specified selector.
 */
Cypress.Commands.add('getBySelLike', (selector, ...args) => {
    return cy.get(`[data-test*=${selector}]`, ...args)
})

Cypress.Commands.add('goToAdminOverview', (msg) => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.adminUrl);
        cy.url().should('contain', settings.adminUrl)
    })
})

Cypress.Commands.add('goToCategoryOverview', (msg) => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.categoryUrl);
        cy.url().should('contain', settings.categoryUrl)
    })
})

Cypress.Commands.add('goToRegister', (msg) => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.registerUrl);
        cy.url().should('contain', settings.registerUrl)
    })
})

Cypress.Commands.add('goToProfile', (msg) => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.profileUrl);
        cy.url().should('contain', settings.profileUrl)
    })
})

Cypress.Commands.add('goToCart', (msg) => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.cartUrl);
        cy.url().should('contain', settings.cartUrl)
    })
})

Cypress.Commands.add('goToDetailView', (msg) => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.detailUrl);
        cy.url().should('contain', settings.detailUrl)
    })
})
