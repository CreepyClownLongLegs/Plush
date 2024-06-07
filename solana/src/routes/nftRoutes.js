const express = require('express');
const Controller = require('../controllers/nftController');

const router = express.Router();

router.post('/:mintToken', Controller.mintAndTransfer);
router.get('/:publicKey', Controller.getAllToken);

module.exports = {
    path: '/nft',
    router: router
};