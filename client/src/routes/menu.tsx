import { Table, TableData } from '@mantine/core';
import { menuItems } from '../test-data';

function Menu() {
  const tableData: TableData = {
    head: ['ID', 'Name', 'Price'],
    body: menuItems.map((item) => Object.values(item)),
  };

  return (
    <Table
      striped
      highlightOnHover
      stickyHeader
      withTableBorder
      withColumnBorders
      data={tableData}
    />
  );
}

export default Menu;
