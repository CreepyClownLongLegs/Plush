const ConnectionService = require('./connectionService');
const { createMint, getMint } = require('@solana/spl-token');
const { PublicKey } = require('@solana/web3.js');
// See https://developers.metaplex.com/token-metadata/mint to add more information to the mints/metadata.
const SmartContractService = {
    /**
     * Creates a new Mint and returns the token of the mint.
     * @returns the token of the mint ( the public Key as Base58).
     */
    createSmartContract: async () => {
        const connection = await ConnectionService.connect();
        const wallet = ConnectionService.wallet();
        const mint = await createMint(
            connection,
            wallet,
            wallet.publicKey,
            wallet.publicKey,
            0
        );
        console.log("Mint: ", mint);
        return mint.toBase58();
    },

    /**
     * Returns information about the mint.
     * @param {String} mintPublicKey the public Key of the mint to get the info from.
     * @returns the mint info.
     */
    getMintInfo: async (mintPublicKey) => {
        const connection = await ConnectionService.connect();
        const key = new PublicKey(mintPublicKey)
        console.log("mintKey: ", key);
        const mintInfo = await getMint(
            connection,
            key
        )

        console.log(mintInfo.supply);
        return mintInfo;
    }
};

module.exports = SmartContractService;