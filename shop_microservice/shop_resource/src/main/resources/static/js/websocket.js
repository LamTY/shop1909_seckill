/**
 * 初始化websocket
 */
var ws;
var heartTime = 2000;
var closeTime = 5000;
var reconnTime = 5000;
function initWs(callback) {
    if(callBack.heartTime){
        heartTime = callBack.heartTime;
    }

    if(callBack.closeTime){
        closeTime = callBack.closeTime;
    }

    if(callBack.reconnTime){
        reconnTime = callBack.reconnTime;
    }
    if(window.WebSocket){
        ws = new WebSocket(callback.url)

        //设置回调
        ws.onopen = function () {
            console.log("服务器连接成功")
            heartBeat()

            closeConn();

            if(callback.myopen){
                callback.myopen();
            }
        }

        ws.onclose = function () {
            console.log("服务器连接断开")
            if(heartTimeout){
                clearTimeout(heartTimeout);
                heartTimeout = null;
            }
            setTimeout(function () {
                reconn(callback);
            }, reconnTime)

            if(callback.myclose){
                callback.myclose();
            }
        }

        ws.onerror = function () {
            console.log("服务器连接异常")
            if(callback.myerror){
                callback.myerror();
            }
        }

        ws.onmessage = function (msg) {
            console.log("接收到服务器消息" + msg.data)
            var msgobj = JSON.parse(msg.data)
            if(msgobj.type == 2){
                clearTimeout(closeTimeout);
                closeConn();
            }
            if(callback.mymessage){
                callback.mymessage(msgobj);
            }
        }
    }
}

/**
 * 关闭连接
 */
var closeTimeout = null;
function closeConn() {
    closeTimeout = setTimeout(function () {
        if (ws){
            ws.close;
        }
    }, closeTime)
}
/**
 * 重连机制
 */
function reconn(callback) {
    initWs(callback)
}

/**
 * 心跳机制
 */
var heartTimeout;
function heartBeat(callback) {
    /编辑心跳信息
    var msg = {"type":2};
    console.log("触发了一次心跳机制")
    sendObjectMsg(msg);
    //发送消息
    heartTimeout = setTimeout(function () {
        heartBeat();
    }, heartTime)
}

/**
 * 发送心跳信息为字符串
 */
function sendMsg(msg) {

    if (ws){
        ws.send(msg);
    }else {
        alert("发送失败")
    }

}

/**
 * 发送心跳信息为对象
 */
function sendObjectMsg(msg) {
    var msgStr = JSON.stringify(msg);
    sendMsg(msgStr)
}