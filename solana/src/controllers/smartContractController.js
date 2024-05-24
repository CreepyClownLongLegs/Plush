const SmartContractService = require('../services/smartContractService');

const SmartContractController = {
    createSmartContract: async (req, res) => {
        try {
            const smartContract = req.body;
            const response = await SmartContractService.createSmartContract(smartContract);
            res.json(response);
        } catch (error) {
            console.log(error);
            res.status(500).json({ error: error.message });
        }
    },

    getMintInfo: async (req, res) => {
        try {
            const mintToken = req.params.mintToken;
            const response = await SmartContractService.getMintInfo(mintToken);
            res.json(response);
        } catch (error) {
            console.log(error);
            res.status(500).json({ error: error.message });
        }
    }
};

module.exports = SmartContractController;