context('search', () => {
    it('should load plush toys on search', () => {
        cy.visit('/');
        cy.get('#search-field').type('Tiger{enter}');
        cy.get('.card').should('have.length.greaterThan', 0);
    });

    it('should not load plush toys on search', () => {
        cy.visit('/');
        cy.get('#search-field').type('NonExistantToy{enter}');
        cy.get('.card').should('have.length', 0);
    });
});
