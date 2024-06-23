context('category', () => {
    beforeEach(() => {
        cy.loginAdmin();
        cy.goToCategoryOverview();
    });

    it("should create a new category", () => {
        cy.getBySel('addCategoryButton').click();
        cy.get('input[name="name"]').type("New Test Category");
        cy.getBySel('createButton').click();

        cy.getBySel('categoryInfo').last().should('be.visible');
        cy.getBySel('categoryInfo').last().should('contain', 'New Test Category');
    });

    it('should delete a category on button press', () => {
        cy.getBySel("deleteButton").its('length').then((num) => {
            cy.getBySel("deleteButton").last().click();
            cy.getBySel("confirmModal").first().click();
            cy.getBySel("deleteButton").should("have.length", num - 1);
        });

        cy.get('.ng-trigger').should("contain", "Category deleted successfully");
    });
});
