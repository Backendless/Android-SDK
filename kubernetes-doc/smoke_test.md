# Backendless Smoke Test 

1. Open API domain in browser. Expected result is
```
All works!  <last commit hash> build id is <build-id>
```

2. Open backendless console and register developer

3. Login to backendless console
4. Create table in the data service
5. Open rest console for the new table put the following body `{}` and press `POST` button. Expected result is created obeject 
6. Check taskman. Go to Backendless Console -> Manage -> Export press export data, you should receive the later in couple minutes that says that export is success 
7. Check java coderunner. Go toBackendless Console -> Bussiness Logic and crete java service sample:
![](img/create_java_test_service.png)
make sure that service is working:
![](img/java_api_result.png)

8. Check js coderunner. Go to Backendless Console -> Bussiness Logic -> Codeless:
![](img/check_js_deploy.png)
make sure that service is working:
![](img/js_api_result.png)

9. Check RT Server. There should be 2 tests: 
- Go to Backendless Console -> Messaging and send some message:
![](img/check_rt.png)
- Go to Backendless Console -> Management -> Log Management -> REAL-TIME LOGGING
You should see https://take.ms/AsVay

