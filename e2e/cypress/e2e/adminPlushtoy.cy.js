context('plushtoy', () => {

    it('should get delete on button press', () => {
        cy.loginAdmin();
        cy.url().should('contain', '/')
        cy.goToAdminOverview();
        cy.getBySel("deleteButton").its('length').then((num) => {
            cy.getBySel("deleteButton").first().click()
            cy.getBySel("deleteButton").should("have.length", num - 1)
        })
    })

});
