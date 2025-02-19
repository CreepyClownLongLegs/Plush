context('search', () => {
    it('should load plush toys on search', () => {
        cy.visit('/');
        cy.get('#search-field').type('Quaki{enter}');
        cy.get('.card').should('have.length.greaterThan', 0);
    });

    it('should not load plush toys on search', () => {
        cy.visit('/');
        cy.get('#search-field').type('NonExistantToy{enter}');
        cy.getBySel('404msg').should('be.visible');
    });
});
