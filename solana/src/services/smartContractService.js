const ConnectionService = require('./connectionService');
const { getMint } = require('@solana/spl-token');
const { PublicKey } = require('@solana/web3.js');
const { createV1, TokenStandard, fetchDigitalAsset } = require('@metaplex-foundation/mpl-token-metadata')
const { generateSigner, percentAmount } = require('@metaplex-foundation/umi')
const { updateV1, fetchMetadataFromSeeds, } = require('@metaplex-foundation/mpl-token-metadata')

// See https://developers.metaplex.com/token-metadata/mint to add more information to the mints/metadata.
const SmartContractService = {
    /**
     * Creates a new Mint and returns the token of the mint.
     * @returns the token of the mint ( the public Key as Base58).
     */
    createSmartContract: async (smartContract) => {
        const wallet = ConnectionService.wallet();
        const umi = await ConnectionService.getUmi();

        const mint = generateSigner(umi)
        await createV1(umi, {
            mint,
            authority: wallet,
            name: smartContract.name,
            uri: smartContract.url,
            sellerFeeBasisPoints: percentAmount(5.5),
            tokenStandard: TokenStandard.NonFungible,
        }).sendAndConfirm(umi)


        return mint.publicKey;
    },

    /**
     * Updates the data of the mint.
     * @param {String} mintToken the public Key as Base58 string of the mint to update the data.
     * @param {Object} data the data to update.
     * @returns the updated data.
     * */
    updateSmartContractData: async (mintToken, data) => {

        const mintPublicKey = new PublicKey(mintToken);
        const wallet = ConnectionService.wallet();
        const umi = await ConnectionService.getUmi();

        const initialMetadata = await fetchMetadataFromSeeds(umi, { mintPublicKey })
        await updateV1(umi, {
            mintPublicKey,
            authority: wallet,
            data: { ...initialMetadata, ...data },
        }).sendAndConfirm(umi)

        return { ...initialMetadata, ...data };
    },

    /**
     * Returns information about the mint.
     * @param {String} mintPublicKey the public Key of the mint to get the info from.
     * @returns the mint info.
     */
    getMintInfo: async (mintPublicKey) => {
        const connection = await ConnectionService.connect();
        const umi = await ConnectionService.getUmi();
        const key = new PublicKey(mintPublicKey)
        console.log("mintKey: ", key);
        const mintInfo = await getMint(
            connection,
            key
        )

        console.log(mintInfo);
        // fetch metaplex info
        const asset = await fetchDigitalAsset(umi, key)
        console.log(asset)
        return { basic: mintInfo, metaplex: asset };
    }
};

module.exports = SmartContractService;