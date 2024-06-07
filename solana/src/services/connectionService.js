const { Connection, clusterApiUrl } = require('@solana/web3.js');
const clusterName = process.env.CLUSTER_API_URL || 'devnet'
const clusterUri = process.env.CLUSTER_API_URL || 'https://api.devnet.solana.com'
const { createUmi } = require('@metaplex-foundation/umi-bundle-defaults')
const { keypairIdentity } = require('@metaplex-foundation/umi')
const { mplTokenMetadata } = require('@metaplex-foundation/mpl-token-metadata')
const bs58 = require('bs58');
const ConnectionService = {
    wallet: function () {
        if (process.env.NODE_ENV === 'development') {
            return require('./airDropService').getWallet();
        } else {
            throw new Error('Wallet not initialized for production')
        }
    },
    connection: undefined,
    connect: async function () {
        if (this.connection) {
            return this.connection;
        }
        else {
            this.connection = new Connection(clusterApiUrl(clusterName), 'confirmed');
            return this.connection;
        }
    },
    umi: undefined,
    getUmi: async function () {
        if (this.umi) {
            return this.umi;
        }
        const umi = createUmi(clusterUri)
            .use(mplTokenMetadata())

        const privateKeyInBase58 = process.env.WALLET_PRIVATE_KEY;
        const keypair = umi.eddsa.createKeypairFromSecretKey(new Uint8Array(bs58.decode(privateKeyInBase58)))
        umi.use(keypairIdentity(keypair))

        this.umi = umi
        return umi

    }
};

module.exports = ConnectionService;