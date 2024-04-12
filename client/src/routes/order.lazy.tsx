import { Stack, Table, Box, Button } from '@mantine/core';
import { useContext } from 'react';
import { createLazyFileRoute } from '@tanstack/react-router';
import { OrderContext } from './__root';

export const Route = createLazyFileRoute('/order')({
  component: Order,
});

function Order() {
  const orderData = useContext(OrderContext);

  const rows = orderData?.items
    ? Object.entries(orderData?.items).map((orderItem) => (
        <Table.Tr key={orderItem[0]}>
          <Table.Td>{orderItem[0]}</Table.Td>
          <Table.Td></Table.Td>
          <Table.Td>{orderItem[1]}</Table.Td>
        </Table.Tr>
      ))
    : [];

  function handleCreate() {
    (async () => {
      try {
        if (!orderData?.items) return;
        const items = Object.entries(orderData?.items).map((item) => ({
          menuItemId: Number(item[0]),
          quantity: item[1],
        }));

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
        const data = await res.json();
        console.log(data);
      } catch (error) {
        console.log(error);
        return;
      }
    })();
  }

  return (
    <Stack>
      <Table
        striped
        highlightOnHover
        stickyHeader
        withTableBorder
        withColumnBorders
        data={{
          head: ['ID', 'Quantity'],
          body: orderData?.items && Object.entries(orderData?.items),
        }}
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
      <Box>{JSON.stringify(orderData)}</Box>
      <Button onClick={handleCreate}>Create</Button>
    </Stack>
  );
}
