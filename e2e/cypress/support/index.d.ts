declare namespace Cypress{
    interface Chainable {
        /**
         * Navigate to main page and login as admin
         */
        loginAdmin();

        /**
         * Navigate to main page and login as user
         */
        loginUser();
    }
}
