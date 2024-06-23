context('detail-view', () => {

    it('should display cards on the main page', () => {
        cy.visit('/');
        cy.get('.card').should('be.visible');
    });

    it('should navigate to detail view when card is clicked', () => {
        cy.visit('/');
        cy.get('.card').first().click();
        cy.url().should('contain', '/detail/1');
    });

    it('should load plush toy details', () => {
        cy.visit('/#/detail/1');
        cy.getBySel('plushToyName').should('contain', 'TEST TIGER 0 Nr.1');
        cy.getBySel("plushToyDesc").should('contain', 'Cute lil gent');
        cy.getBySel("plushToyWeight").should('contain', '0kg');
        cy.getBySel("plushToySize").should('contain', 'MEDIUM');
        cy.getBySel("plushToyHealth").should('contain', '100HP');
        cy.getBySel("plushToyPrice").should('contain', '0.01 SOL');

        cy.getBySel('buyNowButton').contains('BUY NOW!!!').should('be.visible').and('be.enabled');
        cy.getBySel('addToCartButton').contains('ADD TO BAG').should('be.visible').and('be.enabled');
        cy.getBySel('goBackButton').contains('GO BACK').should('be.visible').and('be.enabled');

        cy.get('img').should('have.attr', 'src')
    });
});
