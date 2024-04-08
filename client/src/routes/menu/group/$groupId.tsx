import { createFileRoute } from '@tanstack/react-router';
import { MenuItem } from '../../../types';
import { useContext, useEffect } from 'react';
import QuantityInput from '../../../QuantityInput';
import { Table } from '@mantine/core';
import { OrderContext } from '../../__root';

async function fetchMenuGroup(groupId: string) {
  const res = await fetch(`/api/outlet/menu/group/${groupId}`);
  if (!res.ok) throw new Error(res.statusText);
  return (await res.json()) as MenuItem[];
}

export const Route = createFileRoute('/menu/group/$groupId')({
  component: Group,
  beforeLoad: () => {
    return { title: `Group` };
  },
  loader: async ({ params }) => {
    return fetchMenuGroup(params.groupId);
  },
});

function Group() {
  const orderData = useContext(OrderContext);
  const menuItems = Route.useLoaderData();

  useEffect(() => {
    console.log(orderData);
  }, [orderData]);

  const rows = menuItems.map((item) => (
    <Table.Tr key={item.id}>
      <Table.Td>{item.id}</Table.Td>
      <Table.Td>{item.name}</Table.Td>
      <Table.Td>{item.price}</Table.Td>
      <Table.Td>
        <QuantityInput
          value={orderData?.items[item.id]}
          onChange={(value) =>
            orderData?.setOrderQuantity(item.id, Number(value))
          }
        />
      </Table.Td>
    </Table.Tr>
  ));

  return (
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
          <Table.Th>Price</Table.Th>
          <Table.Th></Table.Th>
        </Table.Tr>
      </Table.Thead>
      <Table.Tbody>{rows}</Table.Tbody>
    </Table>
  );
}
