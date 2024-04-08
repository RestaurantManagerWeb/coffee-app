import { Stack, Table, Box } from '@mantine/core';
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
    </Stack>
  );
}
