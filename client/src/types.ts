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
