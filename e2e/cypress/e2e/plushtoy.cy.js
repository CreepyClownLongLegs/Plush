context('plushtoy', () => {
    beforeEach(() => {
        cy.loginAdmin();
        cy.goToAdminOverview();
    });

    it("should create a new plush toy", () => {
        cy.getBySel('createButton').click()

        cy.get('input[name="name"]').type("New Test Tiger");
        cy.get('input[name="price"]').type("10");
        cy.get('textarea[name="description"]').type("This is a new Test Tiger generated by e2e");
        cy.get('input[name="taxClass"]').type("1");
        cy.get('input[name="weight"]').type("10");
        cy.get('select[name="color"]').select("RED");
        cy.get('select[name="size"]').select("SMALL");
        cy.get('input[name="hp"]').type("100");
        cy.get('input[name="strength"]').type("10");
        cy.getBySel('submitButton').click();

        // workaround since httpRes = 500 error on creation, but plush toy gets created anyway.
        // fails without wait() before and after
        cy.wait(1000);
        cy.goToAdminOverview();
        cy.wait(1000);

        cy.getBySel('plushToyInfo').last().should('be.visible');
        cy.getBySel('plushToyInfo').last().should('contain', 'New Test Tiger', 'This is a new Test Tiger generated be e2e', 10);

        // inform the user about creation, missing in the frontend
        // cy.get('.ng-trigger').should("contain", "PlushToy created with id");
    });

    it("should edit an existing plush toy", () => {
        cy.getBySel('editButton').last().click();

        cy.get('input[name="name"]').clear().type("Updated Test Tiger");
        cy.get('textarea[name="description"]').clear().type("This is an updated Test Tiger generated by e2e");
        cy.get('input[name="price"]').clear().type("20");
        cy.get('input[name="taxClass"]').clear().type("2");
        cy.get('input[name="weight"]').clear().type("15");
        cy.get('select[name="color"]').select("BLUE");
        cy.get('select[name="size"]').select("MEDIUM");
        cy.get('input[name="hp"]').clear().type("150");
        cy.get('input[name="strength"]').clear().type("15");
        cy.getBySel('submitButton').click();

        cy.getBySel("plushToyInfo").should('contain', 'Updated Test Tiger', 'This is a updated Test Tiger generated be e2e', 20);

        //inform the user about edit, missing in the frontend
        //cy.get('.ng-trigger').should("contain", "PlushToy edited with id");
    });

    it('should get delete on button press', () => {
        cy.getBySel("deleteButton").its('length').then((num) => {
            cy.getBySel("deleteButton").last().click();
            cy.getBySel("deleteButton").should("have.length", num - 1);
        });
    });
});
