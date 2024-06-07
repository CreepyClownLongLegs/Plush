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
            replaceBigInt(response);
            res.json(response);
        } catch (error) {
            console.log(error);
            res.status(500).json({ error: error.message });
        }
    },

    updateMintInfo: async (req, res) => {
        try {
            const mintToken = req.params.mintToken;
            const response = await SmartContractService.updateMintInfo(mintToken, req.body);
            replaceBigInt(response);
            res.json(response);
        } catch (error) {
            console.log(error);
            res.status(500).json({ error: error.message });
        }
    }
};

function replaceBigInt(obj) {
    for (let key in obj) {
        if (typeof obj[key] === 'bigint') {
            obj[key] = obj[key].toString();
        } else if (typeof obj[key] === 'object' && obj[key] !== null) {
            replaceBigInt(obj[key]);
        }
    }
}

module.exports = SmartContractController;