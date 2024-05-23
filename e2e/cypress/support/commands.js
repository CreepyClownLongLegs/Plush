import base58 from "bs58";
import nacl from "tweetnacl";
Cypress.Commands.add(
    "loginAdmin",
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
        cy.getBySel("connectWallet").click();
        cy.getBySel("logIn").should('be.visible');
        cy.getBySel("logIn").click();
        cy.getBySel("loggedIn").should('be.visible');

    },
);

Cypress.Commands.add('createMessage', (msg) => {
    cy.fixture('settings').then(settings => {
        cy.contains('a', 'Message');
        cy.contains('button', 'Add message').click();
        cy.get('input[name="title"]').type('title' + msg);
        cy.get('textarea[name="summary"]').type('summary' + msg);
        cy.get('textarea[name="text"]').type('text' + msg);
        cy.get('button[id="add-msg"]').click();
        cy.get('button[id="close-modal-btn"]').click();

        cy.contains('title' + msg).should('be.visible');
        cy.contains('summary' + msg).should('be.visible');
    })
})

Cypress.Commands.add('goToAdminOverview', (msg) => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.adminUrl);
        cy.url().should('contain', settings.adminUrl)
    })
})

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
