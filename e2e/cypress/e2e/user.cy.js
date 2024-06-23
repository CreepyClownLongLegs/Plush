context('user', () => {
    beforeEach(() => {
        cy.loginUser();
        cy.goToProfile();
    });

    it('login and enter user details', () => {
        cy.getBySel('firstName').clear().type('John');
        cy.getBySel('lastName').clear().type('Doe');
        cy.getBySel('emailAddress').clear().type('john.doe@example.com');
        cy.getBySel('phoneNumber').clear().type('1234567890');
        cy.getBySel('country').clear().type('USA');
        cy.getBySel('postalCode').clear().type('12345');
        cy.getBySel('addressLine1').clear().type('123 Main St');
        cy.getBySel('updateButton').click();

        cy.get('.ng-trigger').should('contain', 'Update successful').click();
    });

    it('login and edit user details', () => {
        cy.getBySel('firstName').clear().type('Jane');
        cy.getBySel('lastName').clear().type('Smith');
        cy.getBySel('emailAddress').clear().type('jane.smith@example.com');
        cy.getBySel('phoneNumber').clear().type('0987654321');
        cy.getBySel('country').clear().type('Canada');
        cy.getBySel('postalCode').clear().type('54321');
        cy.getBySel('addressLine1').clear().type('456 Elm St');
        cy.getBySel('updateButton').click();

        cy.get('.ng-trigger').should('contain', 'Update successful').click();
    });

    it('login and delete user account and check for deletion (cancel)', () => {
        cy.getBySel('firstName').should('have.value', 'Jane');
        cy.getBySel('deleteButton').click();

        cy.get('.modal').should('be.visible');
        cy.getBySel('cancelDeleteButton').should('be.visible').click();
        cy.url().should('contain', '/#/profile');
    });

    it('login and delete user account and check for deletion (confirm)', () => {
        cy.getBySel('firstName').should('have.value', 'Jane');
        cy.getBySel('deleteButton').click();

        cy.get('.modal').should('be.visible');
        cy.getBySel('confirmDeleteButton').should('be.visible').click();
        cy.url().should('contain', '/#/');

        cy.get('.ng-trigger').should('contain', 'Your profile was successfully deleted').click();
    });
});
