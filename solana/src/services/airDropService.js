const ConnectionService = require('./connectionService');
const { Keypair, LAMPORTS_PER_SOL } = require('@solana/web3.js');
const bs58 = require('bs58');



const AirDropService = {
    walletObject: undefined,
    getWallet: function () {
        if (!this.walletObject) {
            const privateKeyInBase58 = process.env.WALLET_PRIVATE_KEY;
            if (!privateKeyInBase58) {
                this.walletObject = Keypair.generate();
            }
            else {
                const privateKeyUint8Array = new Uint8Array(bs58.decode(privateKeyInBase58));
                this.walletObject = Keypair.fromSecretKey(privateKeyUint8Array);
            }
            console.log("wallet public key: ", this.walletObject.publicKey.toBase58());
        }
        return this.walletObject;
    },
    airdropSOL: async function () {

        const connection = await ConnectionService.connect();


        const balance = await connection.getBalance(this.getWallet().publicKey);
        console.log("Balance: ", balance);
        if (balance <= 1000) {
            console.log("requesting Airdrop...")
            const airdropSignature = await connection.requestAirdrop(
                this.getWallet().publicKey,
                LAMPORTS_PER_SOL,
            );
            console.log("Airdrop Signature: ", airdropSignature);
        }
    },

    getBalance: async () => {
        const connection = await ConnectionService.connect();
        const balance = await connection.getBalance(this.getWallet().publicKey);
        return balance;
    }
};

module.exports = AirDropService;