const ConnectionService = require('./connectionService');
const { TOKEN_PROGRAM_ID, AccountLayout } = require('@solana/spl-token');
const { PublicKey } = require('@solana/web3.js');
const { TokenStandard, mintV1 } = require('@metaplex-foundation/mpl-token-metadata')
const NftService = {

    mintAndTransfer: async (mintToken, nftTransferRequest) => {
        const wallet = ConnectionService.wallet();
        const mint = new PublicKey(mintToken)
        const umi = await ConnectionService.getUmi();

        console.log("mint", mint.toBase58())
        console.log(nftTransferRequest.publicKey)
        const toAccountKey = new PublicKey(nftTransferRequest.publicKey)

        await mintV1(umi, {
            mint: mint,
            authority: wallet,
            amount: 1,
            tokenOwner: toAccountKey,
            tokenStandard: TokenStandard.NonFungible,
        }).sendAndConfirm(umi)

        return nftTransferRequest.publicKey;
    },

    getAllToken: async (publicKey) => {
        const connection = await ConnectionService.connect();


        const tokenAccounts = await connection.getTokenAccountsByOwner(
            new PublicKey(publicKey),
            {
                programId: TOKEN_PROGRAM_ID,
            }
        );
        let result = [];
        console.log("Token                                         Balance");
        console.log("------------------------------------------------------------");
        tokenAccounts.value.forEach((tokenAccount) => {
            const accountData = AccountLayout.decode(tokenAccount.account.data);
            console.log(`${new PublicKey(accountData.mint)}   ${accountData.amount}`);
            result.push({ mint: new PublicKey(accountData.mint).toBase58(), amount: accountData.amount.toString() });
        })

        return result;
    }

};



module.exports = NftService;