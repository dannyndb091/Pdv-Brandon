export class SelectProduct {
    proCode: string;
    proName: string;
    proUnit: string;
    proPrice: number;
    proFinalPrice: number;
    proMaxPercDisc: number;
    invQty: number;
    invRes: number;
    invInc: number;

    constructor(proCode: string, proName: string, proUnit: string, proFinalPrice: number, proMaxPercDisc: number, 
        invQty: number, invRes: number, invInc: number, proPrice: number) {
        this.proCode = proCode;
        this.proName = proName;
        this.proUnit = proUnit;
        this.proFinalPrice = proFinalPrice;
        this.proMaxPercDisc = proFinalPrice;
        this.invQty = invQty;
        this.invRes = invRes,
        this.invInc = invInc;
        this.proPrice = proPrice;
    }
}
