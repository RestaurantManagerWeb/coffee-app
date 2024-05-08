import { createFileRoute } from '@tanstack/react-router';
import { Order } from '../../types';
import { Stack, Table, Text } from '@mantine/core';
import { formatDateLocale } from '../../util';
import MenuItemName from '../../components/MenuItemName';

async function fetchOrderInfo(orderId: string) {
  const res = await fetch(`/api/outlet/order/${orderId}`);
  if (!res.ok) throw new Error(res.statusText);
  return (await res.json()) as Order;
}

export const Route = createFileRoute('/order/$orderId')({
  component: OrderInfo,
  loader: async ({ params }) => fetchOrderInfo(params.orderId),
});

function OrderInfo() {
  const orderInfo = Route.useLoaderData();

  const rows = orderInfo.shoppingCarts.map((i) => (
    <Table.Tr key={i.menuItemId}>
      <Table.Td>{i.menuItemId}</Table.Td>
      <Table.Td>
        <MenuItemName id={i.menuItemId} />
      </Table.Td>
      <Table.Td>{i.quantity}</Table.Td>
    </Table.Tr>
  ));

  return (
    <Stack>
      <Text>ID: {orderInfo.id}</Text>
      <Text>Receipt ID: {orderInfo.receiptId}</Text>
      <Text>Created: {formatDateLocale(orderInfo.createdAt)}</Text>
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
