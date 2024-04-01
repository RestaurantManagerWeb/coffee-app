import { Table, TableData } from '@mantine/core';
import { useEffect, useState } from 'react';
import { MenuItem } from '../types';

function Menu() {
  const [menuItems, setMenuItems] = useState<MenuItem[]>([]);
  useEffect(() => {
    (async () => {
      const groupId = 1;
      let data: MenuItem[] = [];
      try {
        const res = await fetch(`/api/outlet/menu/group?id=${groupId}`);
        data = await res.json();
        console.log(data);
      } catch (error) {
        console.log(error);
        return;
      }
      setMenuItems(data);
    })();
  }, []);

  const tableData: TableData = {
    head: ['ID', 'Name', 'Price'],
    body: menuItems.map((item) => [item.id, item.name, item.price]),
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
