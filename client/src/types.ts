export type MenuGroup = {
  id: number;
  name: string;
};

export type MenuItem = {
  id: number;
  name: string;
  price: number;
  vat: number;
  inStopList: boolean;
  menuGroupId: number;
  stockItemId: number;
  processChartId: number;
};

export type Order = {
  id: number;
  createdAt: string;
  cancelledAt: string;
  receiptId: number;
  shoppingCarts: {
    quantity: number;
    menuItemId: number;
    orderingId: number;
  }[];
};
