package ustc.yyd.bigbrother.data;

public enum MessageType {
    client_register_telescreen,
    client_report_telescreen,
    client_heartBeat_telescreen, // maybe can be replaced by report
    telescreen_confirm_client,
    telescreen_newClient_webserver,
    telescreen_clientChange_webserver,
    telescreen_heartBeat_client,
    telescreen_changeClient_client,
    webserver_changeClient_telescreen

}
