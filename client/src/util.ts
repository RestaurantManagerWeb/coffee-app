export const formatDateLocale = (date: string) =>
  date && new Date(date).toLocaleString();
