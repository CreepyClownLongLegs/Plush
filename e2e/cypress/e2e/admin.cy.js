context('admin', () => {

    it('should update admin status on a given user', () => {
        cy.loginAdmin().then(() => {
            cy.goToAdminOverview();

            cy.getBySel('usersTable').find('tbody tr').should('have.length.greaterThan', 0);
            cy.getBySel('adminButton').not('[disabled]').should('have.length.greaterThan', 0);
            cy.getBySel('adminButton').not('[disabled]').first().click();
            cy.getBySel('adminButton').first().should('be.disabled');
        });
    });

});
