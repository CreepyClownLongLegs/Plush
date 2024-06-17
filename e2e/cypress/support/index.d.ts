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

        /**
         * Creates a message with a given text
         * @param msg the text of the created message
         */
        createMessage(msg: string);
    }
}
