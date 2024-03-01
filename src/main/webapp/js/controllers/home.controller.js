'use strict';

//首页controller
indexModule.controller('homeController',['$scope', '$http', '$q', 'NgTableParams','$timeout','$interval', function ($scope, $http, $q, NgTableParams,$timeout,$interval) {
    var self = this;

    $scope.tableData = {};
    $scope.req = {};

    //form表单
    $scope.formData = {};
    $scope.departId = {};

    $scope.copy=function () {
    self.tableParams = new NgTableParams({'count': 10}, {
        getData: function (params) {
            //清空请求
            initialRequest();
            var allParams = params.url();

            $scope.req.paging.pageNo = allParams.page;
            $scope.req.paging.pageSize = allParams.count;
            var promise = $http.post("rest/mainDesk/page", $scope.req);
            return promise.then(function (resut) {
                var response = resut.data;
                var total = response.otherData[0];
                params.total(total);
                $scope.tableData = response.data;
                $scope.waitNum=total;
                $scope.panduan=true;
                if(total>0){
                    $scope.showTodo = true;
                }
                if($scope.waitNum==0 || $scope.waitNum==null){
                    $scope.waitNum=0;
                    $scope.showPrompt=false
                }else{
                    $scope.showPrompt=true
                }
                function showNotice(){
                    Notification.requestPermission(function(status){
                        //status默认值'default'等同于拒绝 'denied' 意味着用户不想要通知 'granted' 意味着用户同意启用通知
                        if("granted" != status)
                            return;
                        $scope.notify = new Notification("OA系统消息",{
                            dir:'auto',
                            lang:'zh-CN',
                            tag:'sds',
                            sticky:true,
                            requireInteraction:true,
                            icon:'./images/logo60x60.jpg',
                            body:'您目前有'+total+'条消息待处理！'
                        });
                    });
                }
                if($scope.tableData.length!='0'){
                    // clearInterval($scope.timer);
                    showNotice();
                    // $scope.timer = setInterval(function(){
                    //     showNotice();
                    // },10000)
                }else if($scope.tableData.length=='0'){
                    if($scope.notify){
                        $scope.notify.close();
                        // clearInterval($scope.timer);
                    }
                }

                return response.data;
            });

        }
    });
    };
    $scope.copy();

    $scope.paces=function () {
    self.tableParams = new NgTableParams({'count': 10}, {
        getData: function (params) {
            //清空请求
            initialRequest();
            var allParams = params.url();

            $scope.req.paging.pageNo = allParams.page;
            $scope.req.paging.pageSize = allParams.count;
            var promise = $http.post("rest/mainDesk/getProgress", $scope.req);
            return promise.then(function (resut) {
                var response = resut.data;
                var total = response.otherData[0];
                params.total(total);
                $scope.tableDatas = response.data;
                $scope.waitPace=total;
                if($scope.waitPace==0 || $scope.waitPace==null){
                    $scope.waitPace=0;
                    $scope.pacePrompt=false
                }else{
                    $scope.pacePrompt=true
                }
                for(var i=0;i<$scope.tableDatas.length;i++){
                    if($scope.tableDatas[i].status=='99'&&$scope.tableDatas[i].applyTyp>'20'){
                        $scope.tableDatas[i].print=true;
                    }
                    if($scope.tableDatas[i].status=='98'&&$scope.tableDatas[i].applyTyp<='20'){
                        $scope.tableDatas[i].print=true;
                    }
                    if($scope.tableDatas[i].status=='03'){
                        $scope.tableDatas[i].ignore=true;
                    }
                }
                return response.data;
            });

        }
    });
    };


    if($scope.showTodo==true){
        $http.post("rest/mainDesk/getProgress", {paging:{pageNo:1,pageSize:100}}).success(function (res) {
            $scope.tableDatas = res.data;
            $scope.waitPace=res.data.length;
            if($scope.waitPace==0 || $scope.waitPace==null){
                $scope.waitPace=0;
                $scope.pacePrompt=false
            }else{
                $scope.pacePrompt=true
            }
        });
    }else{
        $scope.paces();
    }

    $interval(function () {
        $scope.copy();
    },1000*60*10,-1);

    function judge() {
        if($scope.formData.threeCe==="√"){
            $scope.myColor0=true;
        }else if($scope.formData.threeCe==="×"){
            $scope.myColor1=true;
        }else if($scope.formData.threeCe==="O"){
            $scope.myColor2=true;
        }
        if($scope.formData.isoCe==="√"){
            $scope.myColor3=true;
        }else if($scope.formData.isoCe==="×"){
            $scope.myColor4=true;
        }else if($scope.formData.isoCe==="O"){
            $scope.myColor5=true;
        }
        if($scope.formData.agentCe==="√"){
            $scope.myColor6=true;
        }else if($scope.formData.agentCe==="×"){
            $scope.myColor7=true;
        }else if($scope.formData.agentCe==="O"){
            $scope.myColor8=true;
        }
        if($scope.formData.technicalParam==="√"){
            $scope.myColor9=true;
        }else if($scope.formData.technicalParam==="×"){
            $scope.myColor10=true;
        }else if($scope.formData.technicalParam==="O"){
            $scope.myColor11=true;
        }
        if($scope.formData.aType==="√"){
            $scope.myColor12=true;
        }else if($scope.formData.aType==="-"){
            $scope.myColor13=true;
        }
        if($scope.formData.bType==="√"){
            $scope.myColor14=true;
        }else if($scope.formData.bType==="-"){
            $scope.myColor15=true;
        }
        if($scope.formData.cType==="√"){
            $scope.myColor16=true;
        }else if($scope.formData.cType==="-"){
            $scope.myColor17=true;
        }
    }

    // 待办事项消息的处理与提交
    $scope.process=function (index) {
        $scope.formData = {};
        clearReasonModel();
        var Id=$scope.tableData[index].id;
        $scope.departId = $scope.tableData[index].departId;
        $scope.applyTyp = $scope.tableData[index].applyTyp;
        var url = "";
        if($scope.applyTyp=="punch"){
            url = "rest/mainDesk/"+Id+"/getPunchInfo";
        } else if($scope.applyTyp=="over"){
            url = "rest/mainDesk/"+Id+"/getOverInfo";
        } else if($scope.applyTyp=="out"){
            url = "rest/mainDesk/"+Id+"/getOutInfo";
        } else if ($scope.applyTyp=="trip") {
            url = "rest/mainDesk/"+Id+"/getTripInfo";
        } else if ($scope.applyTyp=="vacation") {
            url = "rest/mainDesk/"+Id+"/getVacationInfo";
        } else if ($scope.applyTyp=="down") {
            url = "rest/mainDesk/"+Id+"/getDownInfo";
        }else if ($scope.applyTyp=="payment") {
            url = "rest/mainDesk/"+Id+"/getPaymentInfo";
        }
        $http.get(url, {}).success(function (data) {
            console.log("data:", data);
            if (data != null && data != undefined) {
                $scope.formData=data;
                $scope.detain=true;
                $scope.disPersonal=true;
                $("#"+$scope.applyTyp+"Modal").modal({show: true, keyboard: false, backdrop: 'static'});
                judge();
                if($scope.formData.leaveTyp=="00"){
                    $scope.formData.leaveTyp="调休"
                }else if($scope.formData.leaveTyp=="01"){
                    $scope.formData.leaveTyp="年假"
                }else if($scope.formData.leaveTyp=="02"){
                    $scope.formData.leaveTyp="事假"
                }else if($scope.formData.leaveTyp=="03"){
                    $scope.formData.leaveTyp="产假"
                }else if($scope.formData.leaveTyp=="04"){
                    $scope.formData.leaveTyp="婚假"
                }else if($scope.formData.leaveTyp=="05"){
                    $scope.formData.leaveTyp="丧假"
                }else if($scope.formData.leaveTyp=="06"){
                    $scope.formData.leaveTyp="病假"
                }else if($scope.formData.leaveTyp=="07"){
                    $scope.formData.leaveTyp="工程调休"
                }else if($scope.formData.leaveTyp=="08"){
                    $scope.formData.leaveTyp="产前假"
                }
            }
        });
    };
    $scope.handleWait=function () {
        var opt;
        var doms = angular.element(document.querySelectorAll(".radio_"+$scope.applyTyp));
        angular.forEach(doms,function(obj,index){
            if(obj.checked){
                opt= obj.value;
            }
        });
        var reason = "同意";
        if(opt=="disagree"){
            reason=document.getElementById("area_"+$scope.applyTyp).value;
        }
        var url;
        if($scope.applyTyp=="payment"){
            url="rest/"+$scope.applyTyp+"/"+opt+"/getNextUser";
        }else{
            url="rest/"+$scope.applyTyp+"/"+opt+"/"+$scope.departId+"/getNextUser";
        }
        $http.post(url,$scope.formData).success(function (data){
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    if(data.data.length>1){
                        $scope.formData.processors = data.data;
                        var chooseId = $scope.formData.processor;
                        if(chooseId==undefined){
                            $scope.disSub=true;
                        }
                        if($scope.applyTyp=="payment"){
                            $("#chooseModalTwo").modal({show: true, keyboard: false, backdrop: 'static'});
                        }else{
                            $("#chooseModal").modal({show: true, keyboard: false, backdrop: 'static'});
                        }

                    } else {
                        var staffId = "undefined";
                        var applyName;
                        if (data.data.length == 1) {
                            if($scope.applyTyp=="payment"){
                                staffId = data.data[0].userId;
                                applyName=data.data[0].userName;
                            } else {
                                staffId = data.data[0].staffId;
                                applyName=data.data[0].name;
                            }
                        }
                        $http.post("rest/"+$scope.applyTyp+"/"+staffId+"/"+opt+"/"+reason+"/approve", $scope.formData).success(function (data) {
                            if (data != null && data != undefined) {
                                if(data.resultCode=="RESULT_SUCCESS") {
                                    $("#"+$scope.applyTyp+"Modal").modal('hide');
                                    $scope.waitNum=$scope.tableData.length;
                                    if($scope.waitNum==0||$scope.waitNum==null){
                                        $scope.showPrompt=false
                                    }else{
                                        $scope.showPrompt=true
                                    }
                                    if(applyName!=undefined){
                                        showOkModal('提示', "提交成功，下一审批人为&nbsp;&nbsp;&nbsp;"+applyName);
                                    }
                                    self.tableParams.reload();
                                } else {
                                    showOkModal('提示', data.message);
                                }
                            }
                        });

                    }
                }
            }
        });
    };
    $scope.waitSolveTrue=true;
    $scope.radioChange=function () {
        var chooseId = $scope.formData.processor;
        if(chooseId!==undefined){
            $scope.disSub=false;
        }
    };
    $scope.radioChange2=function () {
        var chooseId = $scope.formData.processor2;
        if(chooseId!==undefined){
            $scope.disSub2=false;
        }
    };

    $scope.ShowMode="waitSolve";
    if(!$scope.showTodo){
        $scope.ShowMode="pace";
        $scope.paceTrue=true;
    }
    $scope.ChangeShow = function(ShowMode){
        $scope.ShowMode = ShowMode;
        if(ShowMode=="waitSolve"){
            $scope.waitSolveTrue=true;
            $scope.copy();
        }else{
            $scope.waitSolveTrue=false;
        }
        if(ShowMode=="solve"){
            $scope.solveTrue=true;
            self.tableParams = new NgTableParams({'count': 10}, {
                getData: function (params) {
                    //清空请求
                    initialRequest();
                    var allParams = params.url();

                    $scope.req.paging.pageNo = allParams.page;
                    $scope.req.paging.pageSize = allParams.count;
                    var promise = $http.post("rest/mainDesk/pageDone", $scope.req);
                    return promise.then(function (resut) {
                        var response = resut.data;
                        var total = response.otherData[0];
                        params.total(total);
                        if(total>0){
                            $scope.showTodo = true;
                        }
                        $scope.tableDatas = response.data;
                        return response.data;
                    });

                }
            });
        }else{
            $scope.solveTrue=false;
        }
        if(ShowMode=="pace") {
            $scope.paceTrue = true;
            self.tableParams = new NgTableParams({'count': 10}, {
                getData: function (params) {
                    //清空请求
                    initialRequest();
                    var allParams = params.url();

                    $scope.req.paging.pageNo = allParams.page;
                    $scope.req.paging.pageSize = allParams.count;
                    var promise = $http.post("rest/mainDesk/getProgress", $scope.req);
                    return promise.then(function (resut) {
                        var response = resut.data;
                        var total = response.otherData[0];
                        params.total(total);
                        $scope.tableDatas = response.data;
                        $scope.waitPace=total;
                        if($scope.waitPace==0 || $scope.waitPace==null){
                            $scope.waitPace=0;
                            $scope.pacePrompt=false
                        }else{
                            $scope.pacePrompt=true
                        }
                        for(var i=0;i<$scope.tableDatas.length;i++){
                            if($scope.tableDatas[i].status=='99'&&$scope.tableDatas[i].applyTyp>'20'){
                                $scope.tableDatas[i].print=true;
                            }
                            if($scope.tableDatas[i].status=='98'&&$scope.tableDatas[i].applyTyp<='20'){
                                $scope.tableDatas[i].print=true;
                            }
                            if($scope.tableDatas[i].status=='03'){
                                $scope.tableDatas[i].ignore=true;
                            }
                        }
                        return response.data;
                    });

                }
            });
        }else{
            $scope.paceTrue=false;
        }

    };

    //导出pdf
    $scope.printBtn = function (index) {
        var type = $scope.tableDatas[index].applyTyp;
        var id = $scope.tableDatas[index].id;
        if(type<'20'){
            type='vacation';
        } else if(type==='20') {
            type='trip';
        } else if(type==='21') {
            type='out';
        } else if(type==='22') {
            type='over';
        } else if(type==='23') {
            type='punch';
        } else if(type==='30') {
            type='down';
        } else if(type==='31') {
            type='payment';
        }
        $http.post("rest/attendance/"+ type + "/" + id +"/export", JSON.stringify($scope.singles), {responseType: 'blob'}).success(function (data, status, headers) {
            var blob = new Blob([data], {type: "application/vnd.ms-excel"});
            var fileName = headers()["filename"];
            fileName = decodeURI(fileName);
            var a = document.createElement("a");
            document.body.appendChild(a);
            a.download = fileName;
            a.href = URL.createObjectURL(blob);
            a.click();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    //忽略
    $scope.ignoreBtn=function (index) {
        var type=$scope.tableDatas[index].applyTyp;
        var Id=$scope.tableDatas[index].id;
        if(type<'20'){
            type='vacation';
        }else if(type=='20'){
            type='trip';
        }else if(type=='21'){
            type='out';
        }else if(type=='22'){
            type='over';
        }else if(type=='23'){
            type='punch';
        }else if(type=='30'){
            type='down';
        }else if(type=='31'){
            type='payment';
        }
        $http.post("rest/"+type+"/"+Id+"/ignore").success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', '忽略成功！');
                    self.tableParams.reload();
                } else {
                    showOkModal('提示', data.message);
                }
            }
        });
    };

    $scope.submit=function () {
        $scope.hideFrom=true;
        var opt;
        var doms = angular.element(document.querySelectorAll(".radio_"+$scope.applyTyp));
        angular.forEach(doms,function(obj,index){
            if(obj.checked){
                opt= obj.value;
            }
        });
        var reason = "同意";
        if(opt=="disagree"){
            reason=document.getElementById("area_"+$scope.applyTyp).value;
        }
        var staffId;
        var name;
        if($scope.applyTyp=="payment"){
            staffId = $scope.formData.processor2;
            name=document.getElementById("payment"+staffId).innerText;
        } else {
            staffId = $scope.formData.processor;
            name=document.getElementById("pageHome"+staffId).innerText;
        }
        $http.post("rest/"+$scope.applyTyp+"/"+staffId+"/"+opt+"/"+reason+"/approve", $scope.formData).success(function (data) {
            $scope.hideFrom=false;
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    $("#"+$scope.applyTyp+"Modal").modal('hide');
                    $("#chooseModal").modal('hide');
                    $("#chooseModalTwo").modal('hide');
                    showOkModal('提示', "提交成功，下一审批人为&nbsp;&nbsp;&nbsp;"+name);
                    $scope.waitNum=$scope.tableData.length;
                    if($scope.waitNum==0||$scope.waitNum==null){
                        $scope.showPrompt=false
                    }else{
                        $scope.showPrompt=true
                    }
                    self.tableParams.reload();
                } else {
                    showOkModal('提示', data.message);
                }
            }
        });
    };

    $scope.termination=function () {
        showConfirmModal("该操作会直接结束此流程,是否确认执行?", function () {
            $http.post("rest/"+$scope.applyTyp+"/termination", $scope.formData).success(function (data) {
                if (data != null && data != undefined) {
                    if(data.resultCode=="RESULT_SUCCESS") {
                        $("#"+$scope.applyTyp+"Modal").modal('hide');
                        $scope.waitNum=$scope.tableData.length;
                        if($scope.waitNum==0||$scope.waitNum==null){
                            $scope.showPrompt=false
                        }else{
                            $scope.showPrompt=true
                        }
                        self.tableParams.reload();
                    } else {
                        showOkModal('提示', data.message);
                    }
                }
            });
        });
    };

    $scope.viewHistory = function (actId){
        window.open('viewHistory.html?actId='+actId,'','width=800,height=400,left=500,top=200,scrollbars=yes,status=yes')
    };
    $scope.viewOverHistory=function (index) {
        var actId=$scope.tableDatas[index].actId;
        window.open('viewHistory.html?actId='+actId,'','width=800,height=400,left=500,top=200,scrollbars=yes,status=yes')
    };

    // 首页生日消息的读取
    $scope.noticeContent=[];
    $http.get("rest/staff/birthList", {}).success(function (data) {
        if (data != null && data != undefined) {
            if (data.resultCode == "RESULT_SUCCESS") {
                $scope.noticeContent = data.data;
                var noticeNum11= data.data.length;
                $scope.noticeNum1=noticeNum11;
                if($scope.noticeNum1!="0"){
                    $scope.pageShow=false;
                    if($scope.pageShow==false){
                        $scope.pageWord=true
                    }
                }else{
                    $scope.pageShow=true;
                }
                if($scope.showNotice==false){
                    $scope.pageShow=true
                }
            }
        }
    });
    // 首页转正消息的读取
    $scope.noticeContent2=[];
    $http.get("rest/staff/formalList", {}).success(function (data) {
        if (data != null && data != undefined) {
            if (data.resultCode == "RESULT_SUCCESS") {
                $scope.noticeContent2 = data.data;
                var noticeNum21= data.data.length;
                $scope.noticeNum2=noticeNum21;
                if($scope.noticeNum2!="0"){
                    $scope.becomeShow=false;
                    if($scope.becomeShow==false){
                        $scope.becomeWord=true
                    }
                }else{
                    $scope.becomeShow=true;
                }
                if($scope.showNotice==false){
                    $scope.becomeShow=true
                }
            }
        }
    });
    // 首页合同消息的读取
    $scope.noticeContent3=[];
    $http.get("rest/staff/certificateList", {}).success(function (data) {
        if (data != null && data != undefined) {
            if (data.resultCode == "RESULT_SUCCESS") {
                $scope.noticeContent3 = data.data;
                var noticeNum31= data.data.length;
                $scope.noticeNum3=noticeNum31;
                if($scope.noticeNum3!="0"){
                    $scope.contractShow=false;
                    if($scope.contractShow==false){
                        $scope.contractWord=true
                    }
                }else{
                    $scope.contractShow=true;
                }
                if($scope.showNotice==false){
                    $scope.contractShow=true
                }
            }
        }
    });
    // 首页证书消息的读取
    $scope.noticeContent4=[];
    $http.get("rest/staff/contList", {}).success(function (data) {
        if (data != null && data != undefined) {
            if (data.resultCode == "RESULT_SUCCESS") {
                $scope.noticeContent4 = data.data;
                var noticeNum41= data.data.length;
                $scope.noticeNum4=noticeNum41;
                if($scope.noticeNum4!="0"){
                    $scope.diplomaShow=false;
                    if($scope.diplomaShow==false){
                        $scope.diplomaWord=true
                    }
                }else{
                    $scope.diplomaShow=true
                }
                if($scope.showNotice==false){
                    $scope.diplomaShow=true
                }
            }
        }
    });
    //考勤异常提醒
    $scope.noticeContent5=[];
    $http.get("rest/attendance/remindList", {}).success(function (data) {
        if (data !== null && data !== undefined) {
            if (data.resultCode === "RESULT_SUCCESS") {
                $scope.noticeContent5 = data.data;
                $scope.noticeNum5 = data.data.length;
                if($scope.showNotice === false){
                    $scope.abnormalShow = false
                }
                if($scope.noticeNum5 != "0"){
                    $scope.abnormalShow = false;
                    if($scope.abnormalShow === false){
                        $scope.abnormalWord = true
                    }
                }else{
                    $scope.abnormalShow = true
                }
            }
        }
    });
    //告知提醒
    $scope.noticeContent6=[];
    $http.post("rest/notice/list", {}).success(function (data) {
        if (data != null && data != undefined) {
            if (data.resultCode == "RESULT_SUCCESS") {
                $scope.noticeContent6 = data.data;
                var noticeNum61= data.data.length;
                $scope.noticeNum6=noticeNum61;
                if($scope.showNotice==false){
                    $scope.noticeShow=false
                }
                if($scope.noticeNum6!="0"){
                    $scope.noticeShow=false;
                    if($scope.noticeShow==false){
                        $scope.noticeWord=true
                    }
                }else{
                    $scope.noticeShow=true
                }
            }
        }
    });
    // 首页未核销出差信息提醒
    $scope.noticeContent7=[];
    $scope.loadTripRemindList = function () {
        $http.post("rest/trip/getTripRemindList", {}).success(function (data) {
            if (data != null && data != undefined) {
                if (data.resultCode == "RESULT_SUCCESS") {
                    $scope.noticeContent7 = data.data;
                    var noticeNum71= data.data.length;
                    $scope.noticeNum7=noticeNum71;
                    if($scope.noticeNum7!="0"){
                        $scope.tripShow=false;
                        if($scope.tripShow==false){
                            $scope.tripWord=true
                        }
                    }else{
                        $scope.tripShow=true;
                    }
                    if($scope.showNotice==false){
                        $scope.tripShow=true;
                    }
                }
            }
        });
    }
    $scope.loadTripRemindList();

    $scope.notice=function () {
        $("#noticeModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };
    $scope.abnormal=function () {
        $("#pageTabModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };
    $scope.become=function () {
        $("#becomeModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };
    $scope.birth=function () {
        $("#birthModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };
    $scope.diploma=function () {
        $("#diplomaModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };
    $scope.contract=function () {
        $("#contractModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };
    $scope.showTrip=function () {
        $("#showTripModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };
    $scope.setTripInfo = function (index) {
        $scope.tripFormData = angular.copy($scope.noticeContent7[index]);
        $("#showTripFormModal").modal({show: true, keyboard: false, backdrop: 'static'});
    }
    //将所选择的时间格式化
    $scope.onTimeSet = function (newDate, oldDate) {
        $scope.formatterTime();
    };

    $scope.formatterTime = function () {
        if ($scope.tripFormData.tripDate) {
            $scope.tripFormData.tripDate = DateToString2(new Date($scope.tripFormData.tripDate));
        }
        if ($scope.tripFormData.backDate) {
            $scope.tripFormData.backDate = DateToString2(new Date($scope.tripFormData.backDate));
        }
        if($scope.tripFormData.tripDate==$scope.tripFormData.backDate){
            if($scope.tripFormData.tripPart==1){
                $scope.tripFormData.backPart=1
            }
            if($scope.tripFormData.backPart==0){
                $scope.tripFormData.tripPart=0
            }
        }
    };

    //根据出差信息计算日期
    $scope.changDate = function (){
        $scope.formatterTime();
        $http.post("rest/trip/init", $scope.tripFormData).success(function (data) {
            if (data != null && data != undefined) {
                if (data.resultCode == "RESULT_SUCCESS") {
                    $scope.tripFormData = data.data[0];
                } else {
                    $scope.tripFormData = data.data[0];
                    $("#serverErrorModal").modal({show: true});
                }
            }
        });
    };

    $scope.submitTripInfo = function () {
        if($scope.tripFormData.tripDate>$scope.tripFormData.backDate){
            showOkModal('提示', "开始日期不能高于结束日期！");
        } else {
            $scope.hideFrom=true;
            $http.post("rest/common/"+$scope.tripFormData.tripDate+"/"+$scope.tripFormData.backDate+"/"+$scope.tripFormData.tripPart+"/"+$scope.tripFormData.backPart+"/"+$scope.tripFormData.userId+"/"+$scope.tripFormData.id+"/check",{}).success(function (data) {
                if (data != null && data != undefined) {
                    if(data.resultCode=="RESULT_SUCCESS") {
                        $http.post("rest/trip/hrSave", $scope.tripFormData).success(function (data) {
                            if (data != null && data != undefined) {
                                $scope.hideFrom=false;
                                if (data.resultCode == "RESULT_SUCCESS") {
                                    $("#showTripFormModal").modal('hide');
                                    $scope.loadTripRemindList();
                                } else {
                                    showOkModal('提示', data.message);
                                }
                            }
                        }).error(function (data) {
                            $scope.hideFrom=false;
                            $("#showTripFormModal").modal({show: true});
                        });
                    }else {
                        $scope.hideFrom=false;
                        showOkModal('提示', data.message);
                    }
                }
            });
        }
    };

    //打回原因框的显示
    $scope.detain_punch=true;
    $scope.detain_over=true;
    $scope.detain_out=true;
    $scope.detain_trip = true;
    $scope.detain_vacation = true;
    $scope.detain_payment = true;
    $scope.showReason=function(flag){
        if($scope.applyTyp=="punch"){
            $scope.detain_punch=flag;
        } else if ($scope.applyTyp=="over"){
            $scope.detain_over=flag;
        } else if ($scope.applyTyp=="out"){
            $scope.detain_out=flag;
        } else if ($scope.applyTyp=="trip") {
            $scope.detain_trip = flag;
        } else if ($scope.applyTyp=="vacation") {
            $scope.detain_vacation = flag;
        }else if ($scope.applyTyp=="payment") {
            $scope.detain_payment = flag;
        }
        if(flag==true){
            $scope.okEnd=false;
        }else{
            $scope.okEnd=true;
        }
    };

    //发送提醒邮件至处理人
    $scope.remindMail = function (index) {
        var data = $scope.tableDatas[index];
        console.log(data);
        showConfirmModal("该操作会发送邮件至处理人，确定要发送吗?", function () {
            $http.post("rest/attendance/remindMail", data).success(function (data) {
                if (data != null && data != undefined) {
                    showOkModal('提示', data.message);
                }
            }).error(function (data) {
                $("#serverErrorModal").modal({show: true});
            });
        });
    };

    function clearReasonModel(){
        $scope.punchRadioVal="agree";
        $scope.overRadioVal="agree";
        $scope.outRadioVal="agree";
        $scope.tripRadioVal="agree";
        $scope.vacationRadioVal="agree";
        $scope.outRadioVal="agree";

        $scope.detain_punch=true;
        $scope.detain_over=true;
        $scope.detain_out=true;
        $scope.detain_trip=true;
        $scope.detain_vacation=true;
    }

    function initialRequest() {
        $scope.req.paging = {};
        $scope.req.paging.pageNo = 1;
        $scope.req.paging.pageSize = 10;
    }

    $scope.li=true;
    function cut() {
        $scope.li=false;
        $scope.li2=false;
        $scope.li3=false;
        $scope.li4=false;
        $scope.li5=false;
    }
    $scope.pageLi=function(){
        cut();
        $scope.li=!$scope.li;
    };
    $scope.becomeLi=function(){
        cut();
        $scope.li2=!$scope.li2;
    };
    $scope.contractLi=function(){
        cut();
        $scope.li3=!$scope.li3;
    };
    $scope.diplomaLi=function(){
        cut();
        $scope.li4=!$scope.li4;
    };
    $scope.abnormalLi=function(){
        cut();
        $scope.li5=!$scope.li5;
    };
}]);
