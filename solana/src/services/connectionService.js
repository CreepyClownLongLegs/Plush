const web3 = require('@solana/web3.js');
const clusterApiUrl = process.env.CLUSTER_API_URL || 'devnet'

const ConnectionService = {
    wallet: () => {
        if (process.env.NODE_ENV === 'development') {
            return require('./airDropService').getWallet();
        } else {
            throw new Error('Wallet not initialized for production')
        }
    },
    connection: undefined,
    connect: async () => {
        if (this.connection) {
            return this.connection;
        }
        else {
            this.connection = new web3.Connection(web3.clusterApiUrl(clusterApiUrl), 'confirmed');
            return this.connection;
        }
    }
};

module.exports = ConnectionService;