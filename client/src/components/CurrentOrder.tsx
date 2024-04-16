import { Stack, Table, Button } from '@mantine/core';
import { useContext, useMemo } from 'react';
import { OrderContext } from '../routes/__root';
import MenuItemName from './MenuItemName';

function CurrentOrder() {
  const orderData = useContext(OrderContext);

  const filteredItems = useMemo(() => {
    if (!orderData) return [];
    return Object.entries(orderData.items).filter((item) => item[1]);
  }, [orderData]);

  const rows = filteredItems.map((orderItem) => {
    return (
      <Table.Tr key={orderItem[0]}>
        <Table.Td>{orderItem[0]}</Table.Td>
        <Table.Td>
          <MenuItemName id={Number(orderItem[0])} />
        </Table.Td>
        <Table.Td>{orderItem[1]}</Table.Td>
      </Table.Tr>
    );
  });

  function handleCreate() {
    (async () => {
      try {
        const items = filteredItems.map((item) => ({
          menuItemId: Number(item[0]),
          quantity: item[1],
        }));

        if (!items.length) return;

        const body = {
          receiptId: Math.floor(Math.random() * Number.MAX_SAFE_INTEGER),
          shoppingCartItems: items,
        };

        const res = await fetch('/api/outlet/order', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(body),
        });
        if (!res.ok) return;
        orderData?.clear();
      } catch (error) {
        console.log(error);
        return;
      }
    })();
  }

  return (
    <Stack>
      <Button onClick={handleCreate} disabled={filteredItems.length <= 0}>
        Create
      </Button>
      <Table
        striped
        highlightOnHover
        stickyHeader
        withTableBorder
        withColumnBorders
      >
        <Table.Thead>
          <Table.Tr>
            <Table.Th>ID</Table.Th>
            <Table.Th>Name</Table.Th>
            <Table.Th>Quantity</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>{rows}</Table.Tbody>
      </Table>
    </Stack>
  );
}

export default CurrentOrder;
