import { Link, createFileRoute } from '@tanstack/react-router';
import { useEffect, useState } from 'react';
import { Anchor, Table } from '@mantine/core';
import { Order } from '../../types';
import { formatDateLocale } from '../../util';

export const Route = createFileRoute('/order/')({
  component: OrderList,
});

function OrderList() {
  const [orders, setOrders] = useState<Order[]>([]);

  useEffect(() => {
    (async () => {
      try {
        const res = await fetch('/api/outlet/order/date/current');
        if (!res.ok) return;
        const data = await res.json();
        console.log(data);
        setOrders(data);
      } catch (error) {
        console.log(error);
      }
    })();
  }, []);

  const rows = orders.map((o) => (
    <Table.Tr key={o.id}>
      <Table.Td>{o.id}</Table.Td>
      <Table.Td>{o.receiptId}</Table.Td>
      <Table.Td>{formatDateLocale(o.createdAt)}</Table.Td>
      <Table.Td>{formatDateLocale(o.cancelledAt)}</Table.Td>
      <Table.Td>
        <Anchor to={`/order/${o.id}`} component={Link}>
          {`items: ${o.shoppingCarts.length}`}
        </Anchor>
      </Table.Td>
    </Table.Tr>
  ));

  return (
    <Table
      striped
      highlightOnHover
      stickyHeader
      stickyHeaderOffset={59}
      withTableBorder
      withColumnBorders
    >
      <Table.Thead>
        <Table.Tr>
          <Table.Th>ID</Table.Th>
          <Table.Th>Receipt ID</Table.Th>
          <Table.Th>Created</Table.Th>
          <Table.Th>Cancelled</Table.Th>
          <Table.Th></Table.Th>
        </Table.Tr>
      </Table.Thead>
      <Table.Tbody>{rows}</Table.Tbody>
    </Table>
  );
}
