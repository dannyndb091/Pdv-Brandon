export class Mov {
    movType: number;
    movLine: number;
    proCode: string;
    proName: string;
    proQty: number;
    movPu: number;
    movNet: number;
    movDiscount: number;
    movDiscountPerc: number;
    movSubtotal: number;
    movTax: number;
    movTotal: number;
    movCompleted: boolean;

    constructor(movType: number, movLine: number, proCode: string, proName: string, proQty: number,
        movPu: number, movNet: number, movDiscount: number, movDiscountPerc: number, movSubtotal: number,
        movTax: number, movTotal: number, movCompleted: boolean) {
        this.movType = movType;
        this.movLine = movLine;
        this.proCode= proCode;
        this.proName = proName;
        this.proQty = proQty;
        this.movPu = movPu;
        this.movNet = movNet;
        this.movDiscount = movDiscount;
        this.movDiscountPerc= movDiscountPerc;
        this.movSubtotal = movSubtotal;
        this.movTax = movTax;
        this.movTotal = movTotal;
        this.movCompleted = movCompleted;
    }
}
