const express = require('express');
const SmartContactController = require('../controllers/smartContractController');

const router = express.Router();

router.post('/', SmartContactController.createSmartContract);
router.get('/:mintToken', SmartContactController.getMintInfo);
router.post('/:mintToken', SmartContactController.updateMintInfo);

module.exports = {
    path: '/smart-contract',
    router: router
};