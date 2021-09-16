Application demonstrates how user roles work.

  Before Android application start create wtow roles in your app:
1. "ReadWriteRole" with permissions to read and write from Data Service.
2. "ReadOnlyRole" with permissions only to read from Data Service.

  Also verify that "Dynamic scheme" is enabled in Data Service - on first start application will
create required tables.

  When application start you will see two tabs with sign in form on each. This form will sign in or
register user using provided credentials. On sign in to user will be dynamically assigned
"ReadWriteRole" or "ReadOnlyRole" depending on the tab on which sign in was performed.
When one of the roles will be assigned to the user, another one will be unassigned at the same time.
After sign in you will be redirected to page with tasks list. On this page you will be able to check
existing tasks and add new ones (in you signed in with "ReadWriteRole" role).