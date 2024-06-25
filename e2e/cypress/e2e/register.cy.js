context('register', () => {

    beforeEach(() => {
        cy.loginUser();
        cy.goToRegister();
    });

    it('should show errors for missing inputs', () => {
        cy.getBySel('updateButton').click();
        cy.get('.toast-message').should('contain', 'First name is empty');
        cy.get('.toast-message').should('contain', 'Last name is empty');
        cy.get('.toast-message').should('contain', 'Email address is empty');
        cy.get('.toast-message').should('contain', 'Address is empty');
        cy.get('.toast-message').should('contain', 'Postal Code is empty');
        cy.get('.toast-message').should('contain', 'Country is empty');
    });

    it('should show errors for invalid inputs', () => {
        cy.getBySel('firstName').type('John123');
        cy.getBySel('lastName').type('Doe!');
        cy.getBySel('emailAddress').type('john.doe.com');
        cy.getBySel('addressLine1').type('    ');
        cy.getBySel('postalCode').type('abc123');
        cy.getBySel('country').type('C@nad@');
        cy.getBySel('updateButton').click();

        cy.get('.toast-message').should('contain', 'First name is invalid');
        cy.get('.toast-message').should('contain', 'Last name is invalid');
        cy.get('.toast-message').should('contain', 'Email address is invalid');
        cy.get('.toast-message').should('contain', 'Address is invalid');
        cy.get('.toast-message').should('contain', 'Postal Code is invalid');
        cy.get('.toast-message').should('contain', 'Country is invalid');
    });

    it('should successfully update user details with valid inputs', () => {
        cy.getBySel('firstName').type('Alice');
        cy.getBySel('lastName').type('Johnson');
        cy.getBySel('emailAddress').type('alice.johnson@example.com');
        cy.getBySel('addressLine1').type('456 Maple Avenue');
        cy.getBySel('postalCode').type('67890');
        cy.getBySel('country').type('Canada');

        cy.getBySel('updateButton').click();

        cy.get('.toast-message').should('contain', 'You can finish your payment now if you wish');
    });

});
