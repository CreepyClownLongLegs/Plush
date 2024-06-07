const NftService = require('../services/nftService');

const NftController = {
    mintAndTransfer: async (req, res) => {
        try {
            const nftTransferRequest = req.body;
            const response = await NftService.mintAndTransfer(req.params.mintToken, nftTransferRequest);
            res.json(response);
        } catch (error) {
            console.log(error);
            res.status(500).json({ error: error.message });
        }
    },

    getAllToken: async (req, res) => {

        try {
            const response = await NftService.getAllToken(req.params.publicKey);
            res.json(response);
        } catch (error) {
            console.log(error);
            res.status(500).json({ error: error.message });
        }
    }

};

module.exports = NftController;