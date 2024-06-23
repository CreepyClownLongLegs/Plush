context('cart', () => {

    it('should show error if adding to cart without login', () => {
        cy.visit('/#/detail/1');
        cy.getBySel("addToCartButton").click();
        cy.get('.ng-trigger').should('contain', 'Log in to use this button').click();
    });

    it('should add item to cart', () => {
        cy.loginUser();
        cy.goToDetailView();

        cy.getBySel("addToCartButton").click();
        cy.get('.ng-trigger').should('contain', 'Item added to cart')
    });

    it('should verify item is in cart and check stats', () => {
        cy.loginUser();
        cy.goToCart();

        cy.get('.cart-item').should('exist');
        cy.get('.item-name'). should( 'contain', '#1 TEST TIGER 0');
        cy.get('.hp').should( 'contain', '100HP');
        cy.get('.item-price').should( 'contain', '0.01 SOL');
        cy.get('.total-price').should( 'contain', '0.01 SOL');
        cy.get('.finish-payment').should('be.visible').and('be.enabled');
    }) ;

    it('should remove item from cart and verify cart is empty', () => {
        cy.loginUser();
        cy.goToCart();

        cy.get('.cart-item').its('length').then ((num) => {
            cy.get('.remove-btn').last().click();
            cy.get('.cart-item') .should ('have.length', num - 1);
        });
        cy.get('.cart-item').should('not.exist');
        cy.get('.total-price').should( 'contain', '0 SOL');
    });
});