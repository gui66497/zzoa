'use strict';

//staff controller
indexModule.controller('staffController',['$scope', '$http', '$q', 'NgTableParams', '$timeout','staffService','$compile','$templateCache',  function ($scope, $http, $q, NgTableParams, $timeout, staffService, $compile,$templateCache) {

    //高级搜索框内容
    // $scope.customSearch = {};

    // //高级查询
    // $scope.query=function(){
    //     self.tableParams.reload();
    //     self.tableParams._params.page=1;
    // };

    //合并两个请求对象
    function mergeReq() {
        $scope.req = angular.extend({}, $scope.req, $scope.customSearch);
    }

    // 高级搜索框显示和消失
    $scope.showSearchAll=function () {
        $scope.customSearch={onJob:"1"};
        $scope.searchAll = !$scope.searchAll;
    };
    $scope.contentSearch = function () {
        if(document.getElementById("bottomSelect").value=="null"){
            $scope.customSearch.departId=null
        }
        self.tableParams.reload();
        self.tableParams._params.page=1;
    };

    $scope.myKeyupSearch = function (e) {
        var keycode = window.event?e.keyCode:e.which;
        if(keycode == 13){
            $scope.contentSearch();
        }
    };

    //moment.locale('en');        // English
    moment.locale('zh-cn');     // Simplified chinese

    var self = this;

    $scope.tableData = {};
    $scope.req = {};

    function initialRequest() {
        $scope.req = {};
        $scope.req.paging = {};
        $scope.req.paging.pageNo = 1;
        $scope.req.paging.pageSize = 10;
    }
    initialRequest();

    //默认只显示在职员工
    $scope.req.onJob = 1;

    //form表单
    $scope.formData = {};

    self.formalArr = [{id: 1, title: '是'}, {id: 0, title: '否'}];

    self.genderArr = [{id: 1, title: '男'}, {id: 2, title: '女'}];

    self.politicalArr = [{id: '团员', title: '团员'}, {id: '党员', title: '党员'}, {id: '群众', title: '群众'}];

    self.educationArr = [{id: '本科', title: '本科'}, {id: '硕士', title: '硕士'}, {id: '博士', title: '博士'}];

    self.onJobArr = [{id: 1, title: '在职'}, {id: 0, title: '离职'}];

    self.assuranceArr = [{id: 1, title: '是'}, {id: 0, title: '否'}];

    self.remindArr = [{id: 1, title: '是'}, {id: 0, title: '否'}];

    //全选按钮
    self.checkboxes = {
        checked: false,
        items: {}
    };

    //全选按钮监听
    $scope.$watch(function() {
        return self.checkboxes.checked;
    }, function(value) {
        console.log("value：" + value);
        console.log("$scope.tableData:", $scope.tableData);
        angular.forEach($scope.tableData, function (data) {
            self.checkboxes.items[data.staffId] = value;
        });
    });

    //多选按钮监听
    $scope.$watch(function() {
        return self.checkboxes.items;
    }, function(values) {
        console.log("values：", values);
        var trueCount = 0;
        angular.forEach(values, function (value) {
            if (value) {
                trueCount ++;
            }
        });

        //全都是未选中状态
        if (trueCount === 0) {
            self.checkboxes.checked = false;
        } else if (trueCount === $scope.tableData.length) {//全部是选中状态且数量等于当前页数量
            self.checkboxes.checked = true;
        }
    }, true);

    //切换页监听
    $scope.$watch('req.paging.pageNo', function(value) {
        //初始化全选按钮
        self.checkboxes = {
            checked: false,
            items: {}
        };
    });

    $scope.staffId = {};
    $scope.name = {};

    $scope.Refresh=self.tableParams = new NgTableParams({'count': 10, 'filter': {'onJob': "1"}}, {
        getData: function (params) {
            //清空请求
            //合并高级搜索到参数
            mergeReq();
            console.log("params的url内容", params.url());
            var allParams = params.url();

            /*for (var key in allParams) {
                //是筛选值
                if (key.indexOf("filter") > -1) {
                    var columnName = key.substring(key.indexOf("[") + 1, key.length - 1);
                    if($scope.req.onJob!='0'){
                        var value = decodeURI(allParams[key]);
                        $scope.req[columnName] = value;
                    }
                }
            }*/

            $scope.req.paging.pageNo = allParams.page;
            $scope.req.paging.pageSize = allParams.count;

            console.log("req的值为：", $scope.req);

            columnInitial();

            var promise = $http.post("rest/staff/page", $scope.req);
            return promise.then(function (result) {
                var response = result.data;
                var total = response.otherData[0];
                $scope.allContents=total;
                params.total(total);
                $scope.tableData = response.data;
                $scope.nowContents=$scope.tableData.length;
                return response.data;
            });

        }
    });

    function loadStaffs(){
        $http.get("rest/depart/allList", null).success(function (data) {
            if (data != null && data != undefined) {
                $scope.section= data;
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    }
    loadStaffs();

    //获取所有全职员工
    $http.get("rest/staff/all/allSimple").success(function (response) {
        $scope.fullTimeStaffs = [];     //全职
        $scope.partTimeStaffs = [];     //兼职
        if (response !== null && response !== undefined) {
            angular.forEach(response.data, function (staff) {
                if (staff.onJob == 1) {
                    $scope.fullTimeStaffs.push(staff);
                } else if (staff.onJob == 3) {
                    $scope.partTimeStaffs.push(staff);
                }
            });
        }
    }).error(function (data) {
        $("#serverErrorModal").modal({show: true});
    });

    $scope.myCols = [];

    /**
     * 显示列初始化
     */
    function columnInitial() {
        var localCols = [];
        //不显示的列
        var unShowColumnArr = [];
        $http.get("rest/staff/allCol", {}).success(function (res) {
            if (res !== null && res.resultCode === "RESULT_SUCCESS") {
                var allColumns = res.data;
                angular.forEach(allColumns, function (col) {
                    var one = {"columnName": col.columnName, "isShow": col.isShow == 1};
                    localCols.push(one);
                    if (col.isShow == 0) {
                        unShowColumnArr.push(col.columnName);
                    }
                });
                $scope.myCols = localCols;
                //使其隐藏
                angular.forEach(self.cols, function (col, index) {
                    if (unShowColumnArr.indexOf(col.title()) != -1) {
                        self.cols[index].show = function () {
                            return false;
                        };
                    } else {
                        self.cols[index].show = function () {
                            return true;
                        };
                    }
                });
            }
        });
    }

    /**
     * 监听checkbox变化
     */
    $scope.$watch('myCols', function (newValue, oldValue, scope) {
        if (newValue.length == 0 || oldValue.length == 0) {
            return;
        }
        var keepGoing = true;
        angular.forEach(newValue, function (data, index) {
            if (keepGoing) {
                //找到改变的列
                if (data.isShow != oldValue[index].isShow) {
                    columnUpdate(data);
                    keepGoing = false;
                }
            }
        });
    }, true);

    $scope.fileChanged=function (ele) {
        var filesContent=document.getElementById("file").value;
        document.getElementById("fileValues").value=filesContent.substr(filesContent.lastIndexOf("\\") + 1);
    };

    /**
     * 列修改
     * @param col
     */
    function columnUpdate(col) {
        console.log("需要修改的column：" + col);
        var column = {"columnName": col.columnName, "isShow": col.isShow ? 1 : 0};
        $http.put("rest/staff/updateCol", column).success(function (res) {
            console.log(res);
            if (res.resultCode == "RESULT_SUCCESS") {
                self.tableParams.reload();
            }
        });

    }

    $scope.isShow = false;
    $scope.hideOrShow = function () {
        $scope.isShow = !$scope.isShow;
        if ($scope.isShow == true) {
            $scope.nomalBtn = {
                "margin-top": "20px"
            }
        } else {
            $scope.nomalBtn = {
                "margin-top": "20px"
            }
        }
    };

    //打开新增员工模态框
    $scope.showAddStaffModal = function () {
        $scope.formData = {};
        $scope.formData.onJob = "1";
        $("#staffModalTitle").html("新增员工信息");
        angular.element("#addStaffModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    //简历管理模态框
    $scope.resumeConsult=function (index) {
        $scope.staffId = $scope.tableData[index].staffId;
        $scope.name = $scope.tableData[index].name;
        angular.element("#resumeModal").modal({show: true, keyboard: false, backdrop: 'static'});
        document.getElementById("resumeFile").onchange = function() {
            var fileUser=document.getElementById("resumeFile").value;
            $scope.formData.resumeName= fileUser.substr(fileUser.lastIndexOf("\\")+1);
            document.getElementById("userdefinedFile").value=$scope.formData.resumeName;
        };
        if($scope.tableData[index].resumeName==''||$scope.tableData[index].resumeName==undefined){
            $scope.disButton=true;
            $scope.disInput=false;
        }else{
            $scope.disButton=false;
            $scope.disInput=true;
        }

    };
    
    $scope.resumeStaff = function () {
        var files = document.getElementsByName('resumeFile')[0].files;
        if (files.length < 1) {
            showOkModal("提示信息", "请先选择文件！");
            return;
        }
        var fileTyp = files[0].name.substr(files[0].name.lastIndexOf("."));
        $http({
            method: 'POST',
            url: 'rest/staff/'+$scope.staffId+'/'+fileTyp+'/upload',
            headers: {
                'Content-Type': undefined
            },
            data: {
                file: files[0]
            },
            transformRequest: function (data) {
                var formData = new FormData();
                formData.append('file', data.file);
                return formData;
            }
        }).success(function (data) {
            //请求成功
            showOkModal('提示', "添加成功！");
            self.tableParams.reload();
            angular.element("#resumeModal").modal('hide');
        }).error(function (err, status) {
            console.log(err);
        });
    };

    $scope.downloadResume=function (index) {
        var fileName = $scope.name;
        $http.post("rest/staff/"+$scope.staffId+"/down", $scope.req, {responseType: 'blob'}).success(function (data, status, headers) {
            var blob = new Blob([data], {type: "application/octet-stream"});
            fileName = "简历_" + fileName + "."+headers()["filename"];
            var a = document.createElement("a");
            document.body.appendChild(a);
            a.download = fileName;
            a.href = URL.createObjectURL(blob);
            a.click();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };


    //新增员工
    $scope.addStaff = function () {
        console.log("formdata内容：", $scope.formData);
        if ($scope.formData.id) {
            //跳转到修改
            editStaff();
        } else {
            //入职日期必填
            if (!$scope.formData.entryDate) {
                showOkModal("提示", "请填写入职日期!");
                return;
            }
            //转正日期必填
            if (!$scope.formData.formalDate) {
                showOkModal("提示", "请填写转正日期!");
                return;
            }
            $http.post("rest/staff/add", $scope.formData).success(function (data) {
                if (data != null && data != undefined) {
                    if (data.resultCode == "RESULT_SUCCESS") {
                        showOkModal('提示', "添加成功！");
                        self.tableParams.reload();
                        angular.element("#addStaffModal").modal('hide');

                    } else {
                        showOkModal('提示', data.message);
                    }
                }
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
        }
    };

    // $scope.showEditStaff = function (index) {
    //     $scope.formData = angular.copy($scope.tableData[index]);
    //     $("#staffModalTitle").html("修改员工信息");
    //     angular.element("#addStaffModal").modal({show: true, keyboard: false, backdrop: 'static'});
    // };

    // /**
    //  * 修改员工信息
    //  */
    // function editStaff() {
    //     if ($scope.formData.onJob == "0") {
    //         if (!$scope.formData.leaveTime) {
    //             showOkModal("提示", "离职必须指定离职日期!");
    //             return;
    //         }
    //     }
    //     $http.post("rest/staff/edit", $scope.formData).success(function (data) {
    //         if (data != null && data != undefined) {
    //             showOkModal('提示', data.message);
    //             if (data.resultCode == "RESULT_SUCCESS") {
    //                 self.tableParams.reload();
    //                 angular.element("#addStaffModal").modal('hide');
    //             }
    //         }
    //     }).error(function (data) {
    //         angular.element("#serverErrorModal").modal({show: true});
    //     });
    //
    // }

    $scope.go=function () {
        $scope.req.staffId = null;
        $scope.req.userId = null;
        $scope.allStaff=false;
        $scope.Refresh.reload();
    };
    // 点击姓名出现编辑查看页面
    $scope.staffEditor=function (index) {
        $scope.disPicture=true;
        $scope.basic=false;
        $scope.Job=false;
        $scope.allStaff=true;

        $scope.selectStaff = angular.copy($scope.tableData[index]);
        // angular.element("#editorStaff").modal({show: true, keyboard: false, backdrop: 'static'});
        if($scope.selectStaff.photo==null){
            $scope.imgHead=false;
        }else{
            $scope.imgHead=true;
        }

        $scope.disPersonal=true;
        $scope.personalActive=false;
        $scope.disIncumbency=true;
        $scope.incumbencyActive=false;
        $scope.personalDateOne=true;
        $scope.personalDateTwo=false;
        $scope.incumbencyDateOne=true;
        $scope.incumbencyDateTwo=false;

        $scope.selectStaff.onJob = $scope.selectStaff.onJob + '';
        if($scope.selectStaff.onJob=='1'){
            $scope.selectStaff.onJobStr='在职';
        }else if($scope.selectStaff.onJob=='0'){
            $scope.selectStaff.onJobStr='离职';
        }else if($scope.selectStaff.onJob=='2'){
            $scope.selectStaff.onJobStr='停薪留职';
        }else if($scope.selectStaff.onJob=='3'){
            $scope.selectStaff.onJobStr='兼职(挂职)';
        }
        // 部门的种类
        $http.get("rest/depart/allList",null).success(function (data) {
            if (data != null && data != undefined) {
                $scope.allList=data;
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
        // 职位的种类
        $http.get("rest/dic/RESUME_TYPE/getDicInfo",null).success(function (data) {
            if (data != null && data != undefined) {
                $scope.allDickey=data.data;
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });

    };
    // 在职
    $scope.editPersonal=function () {
        if ($scope.selectStaff.email) {
            $scope.beforeEmail = $scope.selectStaff.email.substr(0, $scope.selectStaff.email.indexOf("@"));
        }
        $scope.as=!$scope.as;
        $scope.Job=!$scope.Job;
        $scope.personalDateOne=!$scope.personalDateOne;
        $scope.personalDateTwo=!$scope.personalDateTwo;
        $scope.disPersonal=!$scope.disPersonal;
        if($scope.disPersonal==true){
            $scope.personalActive=false;
            $scope.selectStaff.email=document.getElementById("before").value+document.getElementById("after").value;
            if($scope.runNumber!=null&&$scope.switchboard!=null){
                $scope.selectStaff.seat=document.getElementById("switchboard").value+'-'+document.getElementById("runNumber").value;
            }

        }else{
            $scope.personalActive=true;
            if($scope.selectStaff.seat!=null){
                $scope.runNumber=$scope.selectStaff.seat.substr($scope.selectStaff.seat.lastIndexOf("-") + 1);
                $scope.switchboard=$scope.selectStaff.seat.substr(0,$scope.selectStaff.seat.indexOf("-"));
            }

        }

        // $scope.onTrial=function () {
        //     var entryMonth=$scope.selectStaff.entryDate.substring(5, 7);
        //     var entryYear=$scope.selectStaff.entryDate.substring(0, 4);
        //     var entryDay=$scope.selectStaff.entryDate.substring(8, 10);
        //     var formalMonth=parseInt(entryMonth)+parseInt($scope.selectStaff.probationPeriod);
        //     if(formalMonth>12){
        //         var formalYear=parseInt(entryYear)+parseInt(1);
        //         var addMonth=parseInt(entryMonth)+parseInt($scope.selectStaff.probationPeriod)-parseInt(12);
        //         $scope.selectStaff.formalDate=formalYear+'-'+addMonth+'-'+entryDay;
        //     }else{
        //         $scope.selectStaff.formalDate=entryYear+'-'+formalMonth+'-'+entryDay;
        //     }
        // };
        //
        // $scope.entryTime=function () {
        //     $timeout(function () {
        //         var entryMonth=$scope.selectStaff.entryDate.substring(5, 7);
        //         var entryYear=$scope.selectStaff.entryDate.substring(0, 4);
        //         var entryDay=$scope.selectStaff.entryDate.substring(8, 10);
        //         var formalMonth=parseInt(entryMonth)+parseInt($scope.selectStaff.probationPeriod);
        //         if(formalMonth>12){
        //             var formalYear=parseInt(entryYear)+parseInt(1);
        //             var addMonth=parseInt(entryMonth)+parseInt($scope.selectStaff.probationPeriod)-parseInt(12);
        //             $scope.selectStaff.formalDate=formalYear+'-'+addMonth+'-'+entryDay;
        //         }else{
        //             $scope.selectStaff.formalDate=entryYear+'-'+formalMonth+'-'+entryDay;
        //         }
        //     },100);
        //
        // };

    };

    // 个人
    $scope.editIncumbency=function () {
        $scope.disPicture=!$scope.disPicture;
        $scope.basic=!$scope.basic;
        $scope.incumbencyDateOne=!$scope.incumbencyDateOne;
        $scope.incumbencyDateTwo=!$scope.incumbencyDateTwo;
        $scope.disIncumbency=!$scope.disIncumbency;
        if($scope.disIncumbency==true){
            $scope.incumbencyActive=false;
        }else{
            $scope.incumbencyActive=true;
        }
        if($scope.disPicture==false){
            $scope.growHand=true;
        }else{
            $scope.growHand=false;
        }

        $scope.information=function () {
            if($scope.selectStaff.idNumber!=null){
                if($scope.selectStaff.idNumber.length==18){
                    var birth=$scope.selectStaff.idNumber.substring(6, 10) + "-" + $scope.selectStaff.idNumber.substring(10, 12) + "-" + $scope.selectStaff.idNumber.substring(12, 14);
                    $scope.selectStaff.birth=birth;
                    if (parseInt($scope.selectStaff.idNumber.substr(16, 1)) % 2 == 1) {
                        $scope.selectStaff.gender="1";
                    } else {
                        $scope.selectStaff.gender="2";
                    }
                    var myDate = new Date();
                    var month = myDate.getMonth() + 1;
                    var day = myDate.getDate();
                    var age = myDate.getFullYear() - $scope.selectStaff.idNumber.substring(6, 10) - 1;
                    if ($scope.selectStaff.idNumber.substring(10, 12) < month || $scope.selectStaff.idNumber.substring(10, 12) == month && $scope.selectStaff.idNumber.substring(12, 14) <= day) {
                        age++;
                    }
                    $scope.selectStaff.age=age;
                }else{
                    $scope.selectStaff.birth=null;
                    $scope.selectStaff.age=null;
                }
            }else{
                $scope.selectStaff.birth=null;
                $scope.selectStaff.age=null;
            }

        };

        if($scope.selectStaff.gender=="1"){
            $scope.selectStaff.gender="1";
        }else{
            $scope.selectStaff.gender="0";
        }
        if($scope.selectStaff.marriage=="1"){
            $scope.selectStaff.marriage="1";
        }else{
            $scope.selectStaff.marriage="0";
        }
        if($scope.selectStaff.hasChildren=="1"){
            $scope.selectStaff.hasChildren="1";
        }else{
            $scope.selectStaff.hasChildren="0";
        }

    };

    // // 点击确认
    // $scope.editorSure=function () {
    //     if($scope.disPersonal==false){
    //         $scope.formData.email=document.getElementById("before").value+document.getElementById("after").value;
    //     }
    //     var myPhone = document.getElementById('phone').value;
    //     var phone=myPhone.substr(0, 11);
    //     var ePhone = document.getElementById('emergencyPhone').value;
    //     var emergencyPhone=ePhone.substr(0, 11);
    //     var idCardNo = document.getElementById('idCardNo').value;
    //     console.log(phone,emergencyPhone);
    //     if(!(/^1[34578]\d{9}$/.test(phone))&&phone!==undefined&&phone!==""||!(/^1[34578]\d{9}$/.test(emergencyPhone))&&emergencyPhone!==undefined&&emergencyPhone!==""){
    //         alert("手机号码错误");
    //         return false;
    //     }else if(!(/^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/.test(idCardNo))&&idCardNo!==undefined&&idCardNo!==""){
    //         alert("身份证输入不合法");
    //         return false;
    //     }else{
    //         $http.post("rest/staff/edit", $scope.formData).success(function (data) {
    //             if (data != null && data != undefined) {
    //                 showOkModal('提示', data.message);
    //                 if (data.resultCode == "RESULT_SUCCESS") {
    //                     self.tableParams.reload();
    //                     angular.element("#editorStaff").modal('hide');
    //                 }
    //             }
    //         }).error(function (data) {
    //             angular.element("#serverErrorModal").modal({show: true});
    //         });
    //     }
    // };


    //批量删除
    $scope.batchDelStaff = function () {
        var idArr = [];
        angular.forEach(self.checkboxes.items, function (data, index) {
            if (data) {
                idArr.push(index);
            }
        });
        if (idArr.length < 1) {
            showOkModal("提示", "请先选择员工！");
            return;
        }
        showConfirmModal("确定要删除吗？", function () {
            $http.delete("rest/staff/" + idArr.join(",") + "/batchDel").success(function (response) {
                if (response.resultCode == "RESULT_SUCCESS") {
                    showOkModal('提示', "删除成功！");
                    self.tableParams.reload();
                }
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
        });
    };

    //弹出确认删除模态框
    $scope.showDeleteStaff = function (index) {
        console.log("当前tableData的值为：", $scope.tableData);
        var staffId = $scope.tableData[index].staffId;
        console.log(staffId);
        showConfirmModal("确认要删除吗？", function () {
            $http.delete("rest/staff/" + staffId + "/del").success(function (response) {
                showOkModal('提示', response.message);
                if (response.resultCode == "RESULT_SUCCESS") {
                    self.tableParams.reload();
                    $("#confirmModal").modal('hide');
                }
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
        });
    };

    //导入员工信息
    $scope.importStaff = function () {
        angular.element("#uploadStaff").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    $scope.upload = function () {
        $scope.hideFrom=true;
        var files = document.getElementsByName('file')[0].files;
        if (files.length < 1) {
            $scope.hideFrom=false;
            showOkModal("提示信息", "请先选择文件！");
            return;
        }
        $http({
            method: 'POST',
            url: 'rest/staff/preUpload',
            headers: {
                'Content-Type': undefined
            },
            data: {
                file: files[0]
            },
            transformRequest: function (data) {
                var formData = new FormData();
                formData.append('file', data.file);
                return formData;
            },
        }).success(function (data) {
            //请求成功
            $scope.hideFrom=false;
            if (data.resultCode == "RESULT_ERROR") {
                showOkModal("提示信息", data.message);
                return;
            }
            $scope.newStaff=data.data;
            $scope.newHaveStaff=[];
            $scope.newNotHaveStaff=[];
            for(var i=0;i<$scope.newStaff.length;i++){
                if($scope.newStaff[i].isExist=="1"){
                    $scope.newHaveStaff.push($scope.newStaff[i])
                }
                if($scope.newStaff[i].isExist=="0"){
                    $scope.newNotHaveStaff.push($scope.newStaff[i])
                }
            }
            angular.element("#uploadChangeStaff").modal({show: true, keyboard: false, backdrop: 'static'});
        }).error(function (err, status) {
            $scope.hideFrom=false;
            console.log(err);
        });
    };

    $scope.m = [];
    $scope.checked = [];
    $scope.selectAll=function () {
        if($scope.select_all) {
            $scope.select_one = true;
            $scope.checked = [];
            angular.forEach($scope.newHaveStaff, function (row, index) {
                $scope.checked.push(row.staffId);
                $scope.m[row.staffId] = true;
            })
        }else {
            $scope.select_one = false;
            $scope.checked = [];
            $scope.m = [];
        }
    };
    $scope.selectOne = function (select) {
        angular.forEach($scope.m , function (row, id) {
            var index = $scope.checked.indexOf(id);
            if(row && index === -1) {
                $scope.checked.push(id);
            } else if (!row && index !== -1){
                $scope.checked.splice(index, 1);
            };
        });
        if ($scope.newHaveStaff.length === $scope.checked.length) {
            $scope.select_all = true;
        } else {
            $scope.select_all = false;
        }
    };

    $scope.n = [];
    $scope.checkedTwo = [];
    $scope.selectAllTwo=function () {
        if($scope.select_all_two) {
            $scope.select_two = true;
            $scope.checkedTwo = [];
            angular.forEach($scope.newNotHaveStaff, function (row, index) {
                $scope.checkedTwo.push(row.staffId);
                $scope.n[row.staffId] = true;
            })
        }else {
            $scope.select_two = false;
            $scope.checkedTwo = [];
            $scope.n = [];
        }
    };
    $scope.selectTwo = function (select) {
        angular.forEach($scope.n , function (row, id) {
            var index = $scope.checkedTwo.indexOf(id);
            if(row && index === -1) {
                $scope.checkedTwo.push(id);
            } else if (!row && index !== -1){
                $scope.checkedTwo.splice(index, 1);
            };
        });
        if ($scope.newNotHaveStaff.length === $scope.checkedTwo.length) {
            $scope.select_all_two = true;
        } else {
            $scope.select_all_two = false;
        }
    };

    $scope.uploadChange=function () {
        $scope.hideFrom=true;
        if($scope.checked.length!==0 && $scope.checkedTwo.length!==0){
           var allRecord=$scope.checked.join()+","+$scope.checkedTwo.join()
        }
        if($scope.checkedTwo.length!==0 && $scope.checked.length==0){
           var allRecord=$scope.checkedTwo.join()
        }
        if($scope.checkedTwo.length==0 && $scope.checked.length!==0){
           var allRecord=$scope.checked.join()
        }
        if(allRecord!==undefined){
            $http.post("rest/staff/" + allRecord + "/upload").success(function (response) {
                $scope.hideFrom=false;
                $("#uploadChangeStaff").modal('hide');
                $("#uploadStaff").modal('hide');
                showOkModal("提示信息", "上传成功！");
                self.tableParams.reload();
            }).error(function (data) {
                $scope.hideFrom=false;
                angular.element("#serverErrorModal").modal({show: true});
            });
        }else{
            $scope.hideFrom=false;
            showOkModal("提示信息", "请先选择上传员工！");
        }
    };

    //导出员工信息前操作
    $scope.toExportStaff = function () {
        angular.element("#exportSelect").modal({show: true});
    };

    //员工导出列定制 点击checkbox触发
    $scope.exportCheck = function (name) {
        console.log("exportCheck", name);
        angular.forEach($scope.myCols, function (col) {
            if (col.columnName === name) {
                col.checked = !col.checked;
            }
        });
        console.log("$scope.myCols", $scope.myCols);
    };

    //导出员工信息全选按钮
    $scope.exportCheckAll = function () {
        angular.forEach($scope.myCols, function (col) {
            col.checked = $scope.exportAllModel;
        });
    };

    //导出员工信息
    $scope.exportStaff = function () {
        var exportArr = [];
        angular.forEach($scope.myCols, function (col) {
            if (col.checked) {
                exportArr.push(col.columnName);
            }
        });
        if (exportArr.length < 1) {
           showOkModal("提示", "请至少选择一列");
           return;
        }
        console.log("exportArr", exportArr);
        $scope.req.exportStr = exportArr.join(",");
        $http.post("rest/staff/export", $scope.req, {responseType: 'blob'}).success(function (data, status, headers) {
            var blob = new Blob([data], {type: "application/vnd.ms-excel"});
            var fileName = headers()["filename"];
            fileName = decodeURI(fileName);
            var a = document.createElement("a");
            document.body.appendChild(a);
            a.download = fileName;
            a.href = URL.createObjectURL(blob);
            a.click();
            angular.element("#exportSelect").modal('hide');
        }).error(function (data) {
            angular.element("#serverErrorModal").modal({show: true});
        });
    };

    //将所选择的时间格式化
    $scope.onTimeSet = function (newDate, oldDate) {
        $scope.formatterTime();
    };

    $scope.formatterTime = function () {
        if ($scope.formData.entryDate) {
            $scope.formData.entryDate = DateToString2(new Date($scope.formData.entryDate));
        }
        if ($scope.formData.formalDate) {
            $scope.formData.formalDate = DateToString2(new Date($scope.formData.formalDate));
        }
        if ($scope.formData.birth) {
            $scope.formData.birth = DateToString2(new Date($scope.formData.birth));
        }
        if ($scope.formData.contractStart) {
            $scope.formData.contractStart = DateToString2(new Date($scope.formData.contractStart));
        }
        if ($scope.formData.contractEnd) {
            $scope.formData.contractEnd = DateToString2(new Date($scope.formData.contractEnd));
        }
        if ($scope.formData.graduateDate) {
            $scope.formData.graduateDate = DateToString2(new Date($scope.formData.graduateDate));
        }
        if ($scope.formData.certificateValidity) {
            $scope.formData.certificateValidity = DateToString2(new Date($scope.formData.certificateValidity));
        }
        if ($scope.formData.leaveTime) {
            $scope.formData.leaveTime = DateToString2(new Date($scope.formData.leaveTime));
        }
    };

    /* 合同开始时间 结束时间组合选择框
     -----------------------------------------------*/
    $scope.endDateBeforeRender = endDateBeforeRender;
    $scope.endDateOnSetTime = endDateOnSetTime;
    $scope.startDateBeforeRender = startDateBeforeRender;
    $scope.startDateOnSetTime = startDateOnSetTime;

    function startDateOnSetTime() {
        $scope.$broadcast('start-date-changed');
        $scope.formatterTime();
    }

    function endDateOnSetTime() {
        $scope.$broadcast('end-date-changed');
        $scope.formatterTime();
    }

    function startDateBeforeRender($dates) {
        if ($scope.formData.contractEnd) {
            var activeDate = moment($scope.formData.contractEnd);

            $dates.filter(function (date) {
                return date.localDateValue() >= activeDate.valueOf()
            }).forEach(function (date) {
                date.selectable = false;
            })
        }
    }

    function endDateBeforeRender($view, $dates) {
        if ($scope.formData.contractStart) {
            var activeDate = moment($scope.formData.contractStart).subtract(1, $view).add(1, 'minute');

            $dates.filter(function (date) {
                return date.localDateValue() <= activeDate.valueOf()
            }).forEach(function (date) {
                date.selectable = false;
            })
        }
    }

    /*-----------------------------------------------*/

    $scope.groupSet = {};
    $scope.groupList = "";

    $scope.setGroup = function (index) {
        var staff = this.row;

        var promise = staffService.getAllGroup();
        promise.then(function (data) {
            $scope.groupList = data;
            $scope.groupSet.userId = staff.userName;

            var groupIdArr = [];
            angular.forEach(staff.groups, function (group, index) {
                groupIdArr.push(group.id)
            });
            $scope.groupSet.groupIdArr = groupIdArr;
            $scope.groupSet.groupIds = groupIdArr.join(",");
            console.log("groupSet:", $scope.groupSet);

            angular.element('#setGroupsModal').modal({show: true});
        });

    };

    /**
     * 判断是否选中
     * @param currentGroupId
     * @returns {boolean}
     */
    $scope.isChecked = function (currentGroupId) {
        if ($scope.groupSet.groupIds.indexOf(currentGroupId) > -1) {
            return true;
        } else {
            return false;
        }
    };

    /**
     * 保存组
     */
    $scope.saveGroup = function () {
        var checkedGroup = [];
        $("input[name=group]:checked").each(function () {
            checkedGroup.push($(this).val());
        });

        var req = {};
        req.staffId = $scope.groupSet.userId;
        req.groupIdList = checkedGroup;

        $http.post("rest/identity/group/set", req).success(function (data) {
            if (data != null && data != undefined) {
                console.log(data);
                if (data.resultCode = "RESULT_SUCCESS") {
                    self.tableParams.reload();
                    angular.element("#setGroupsModal").modal('hide');

                }
            }
        }).error(function (data) {
            angular.element("#serverErrorModal").modal({show: true});
        });

    };

    $scope.treeOptions = {
        accept: function (sourceNodeScope, destNodesScope, destIndex) {
            return true;
        }
    };

    $scope.getTreeList = function () {
        $scope.angularTreeList = [];
        //查询树
        $http.get("rest/depart/list", null).success(function (data) {
            if (data != null && data != undefined) {
                $scope.angularTreeList.push(data[0]);
                $scope.treeOptions.data = $scope.angularTreeList;
            }
        }).error(function (data) {
            angular.element("#serverErrorModal").modal({show: true});
        });
    };

    $scope.getAuthorityList = function () {
        var promise = staffService.getAllGroup();
        return promise.then(function (result) {
            $scope.authorityList = result;
            return result;
        });
    };

    $scope.getAuthorityListByStaffId = function (obj) {
        $scope.userDeparts = [];

        var promise = staffService.getUserAuthorityByStaffId(obj.staffId);
        promise.then(function (data) {
            if (data.resultCode == "RESULT_SUCCESS" && data.data.length > 0) {
                for (var i = 0; i < data.data.length; i++) {
                    $scope.userDeparts.push(data.data[i].departId);
                }
                $scope.userAuthorityData = data.data || null;
            }
        })
    };

    $scope.checkUserAuthority = function (obj) {
        if ($.inArray(obj.departId, $scope.userDeparts) > -1) {
            return true;
        } else {
            return false;
        }
    };

    $scope.clear = function () {
        var doms = angular.element(document.querySelectorAll(".depart_checkBox"));
        angular.forEach(doms, function (item, index) {
            item.checked = false;
        });
    };

    //点击角色权限按钮
    $scope.showAuthority = function (obj) {
        console.log(angular.element(document.querySelectorAll(".depart_checkBox")));
        /*var doms = angular.element(document.querySelectorAll(".depart_checkBox"));
        angular.forEach(doms, function (item, index) {
            item.checked = false;
        })*/;
        angular.element(document.querySelectorAll("role-directive")).remove();
        $scope.user = obj;
        $scope.getAuthorityListByStaffId(obj);
        $scope.getAuthorityList();
        angular.element("#showAuthorityModal").modal({
            show: true
        });
        $timeout(function () {
            var doms = angular.element(document.querySelectorAll(".depart_checkBox"));
            angular.forEach(doms, function (item, index) {
                if (item.checked) {
                    var departName = angular.element("#departCheck" + item.value).attr("departName");
                    var divs = '<role-directive id="roleDir' + item.value + '" model="roleModel"  param="{userAuthorityData:userAuthorityData,staffId:user.staffId,departId:' + item.value + ',departName:\'' + departName + '\'}"></role-directive>';
                    var template = $compile(angular.element(divs))($scope);
                    angular.element("#role_fieldSet").append(template);
                }
            });
        }, 200)
    };
    $scope.authorityArr = [];
    $scope.getCheckBoxVal = function (obj) {
        $scope.roleModel = {};
        var divs = '<role-directive id="roleDir' + obj.departId + '" model="roleModel" param="{staffId:user.staffId,departId:' + obj.departId + ',departName:\'' + obj.departName + '\'}"></role-directive>';
        var template = $compile(angular.element(divs))($scope);
        var tempDom = angular.element(document.querySelector("#departCheck" + obj.departId));
        if (tempDom[0].checked) {
            angular.element("#role_fieldSet").append(template);
            $scope.authorityArr.push($scope.roleModel);
        } else {
            angular.element(document.querySelector("#roleDir" + obj.departId)).remove();
            if (tempDepartList.length > 0) {
                angular.forEach(tempDepartList, function (item, index) {
                    if (item.departId == obj.departId) {
                        tempDepartList.splice(index,1);
                    }
                })
            }
        }
    };

    var tempDepartList = [];
    $scope.$on("toParent", function (event, data) {
        console.log(tempDepartList);
        var flag = false;
        if (tempDepartList.length > 0) {
            for(var i=0;i<tempDepartList.length;i++){
                if (tempDepartList[i].departId == data.departId) {
                    tempDepartList[i] = data;
                    flag = true;
                }else if(data.checked ==true){
                    tempDepartList[i].checked =false;
                }
            }
        }
        if (!flag) {
            tempDepartList.push(data);
        }
        console.log(tempDepartList);
    });

    $scope.addUserAuthority = function () {
        if (tempDepartList.length > 0) {
            var hasDefault = false;
            angular.forEach(tempDepartList, function (departRole, index) {
                if (departRole.checked != null) {
                    hasDefault = true;
                    return false;
                }
            });
        }
        if (!hasDefault) {
            showOkModal("提示", "请选择一个默认部门！");
            return;
        }
        var json = {departRoles : tempDepartList, staffId : $scope.user.staffId};
        staffService.addUserDepartAuthority(json).then(function (data) {
            if (data.resultCode == "RESULT_SUCCESS") {
                showOkModal("提示框", data.message);
                self.tableParams.reload();
                angular.element("#showAuthorityModal").modal("hide");
            } else {
                showOkModal("提示框", data.message);
            }
        })
    };


    //回车事件监听
    $scope.myKeyup = function (e) {
        var keycode = window.event?e.keyCode:e.which;
        if(keycode == 13){
            $scope.query();
        }
    };

    // 标签页打开ldapui
    $scope.jumpToLdapUI = function (e) {
        window.open("http://192.168.11.155:8080/ldapui/","_blank")
    };

}]);