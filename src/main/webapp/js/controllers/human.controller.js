'use strict';

//人事controller
indexModule.controller('humanController',['$scope', '$rootScope', '$http', '$timeout', '$cookieStore',function ($scope, $rootScope, $http, $timeout, $cookieStore) {

    /**
     * 是否忽略考勤删除的时间限制（结算日之前的流程正常是不能删除的）
     * @type {boolean}
     */
    $rootScope.ignoreDeleteLimit = false;

    $scope.contents = [];

    //左侧树的option
    $scope.treeOptions = {
        nodeChildren: "children",
        dirSelectable: false,
        isSelectable: function(node) {
            return !node.isTop;
        },
        injectClasses: {
            "ul": "c-ul",
            "li": "c-li",
            "liSelected": "c-liSelected",
            "iExpanded": "c-iExpanded",
            "iCollapsed": "c-iCollapsed",
            "iLeaf": "c-iLeaf",
            "label": "c-label",
            "labelSelected": "c-labelSelected"
        }
    };
    $scope.treeData1 =
        [
            { "name" : "首页", "id" : "1", "isTop":true, "children" : [
                { "name" : "首页", "id" : "1", "isTop":false, "url": "page/pageHome.html", "children" : [] }
            ]}
        ];

    $scope.treeData = [];

    //考勤菜单栏
    $scope.treeDataCheck = [];

    //商务财务菜单栏
    $scope.treeCommerce = [];

    $scope.leftRight=false;

    /**
     * 选中左侧分类
     * @param node
     */
    $scope.nodeSelect = function (node) {
        if (node.name === '无权限') {
            return;
        }
        if (!isIn(node, $scope.contents)) {
            $scope.contents.push(node);
            bindCloseTabSpan();
            move()
        }
        //激活新加入的tab页,这里必须设置延迟，否则无效，不知为何
        $timeout(function () {
            $scope.activeIndex = node.id;
            if(node.name=='备份还原'){
                $scope.choice='';
                $scope.restore=false;
                $scope.papers='';
                $("#backupRestoreModal").modal({show: true, keyboard: false, backdrop: 'static'});
                $http.post("rest/backUp/getFileList").success(function (data) {
                    if (data != null && data != undefined) {
                        $scope.reduction=data.data;
                    }else {
                        showOkModal('提示', data.message);
                    }
                });
            }
        }, 50);
    };


    $scope.changeStyle=function () {
        if($scope.choice=='1'){
            $scope.restore=true;
        }else {
            $scope.restore=false;
        }
    };

    $scope.telescopicBtn=function () {
        $scope.isRetractBtn=!$scope.isRetractBtn;
    };

    $scope.hideFrom=false;
    $scope.submitBackupRestore=function () {
        $scope.hideFrom=true;
        if($scope.choice=='0'){
            $http.post("rest/backUp/backUp").success(function (data) {
                if (data != null && data != undefined) {
                    if(data.resultCode=="RESULT_SUCCESS") {
                        showOkModal('提示', '备份成功！');
                        $("#backupRestoreModal").modal('hide');
                        var index=$scope.contents.length-1;
                        $scope.contents.splice(index, 1);
                        $scope.hideFrom=false;
                    } else {
                        showOkModal('提示', '备份失败！');
                        $scope.hideFrom=false;
                    }
                }
            });
        }else if($scope.choice=='1'){
            $http.post("rest/backUp/"+$scope.papers+"/reduce").success(function (data) {
                if (data != null && data != undefined) {
                    if(data.resultCode=="RESULT_SUCCESS") {
                        showOkModal('提示', '还原成功！');
                        $("#backupRestoreModal").modal('hide');
                        var index=$scope.contents.length-1;
                        $scope.contents.splice(index, 1);
                        $scope.hideFrom=false;
                    } else {
                        showOkModal('提示', '还原失败！');
                        $scope.hideFrom=false;
                    }
                }
            });
        }else{
            showOkModal('提示', '尚未选择！');
            $scope.hideFrom=false;

        }
    };

    $scope.cancel=function () {
        var index=$scope.contents.length-1;
        $scope.contents.splice(index, 1);
    };

    // 判断all是否包含one
    function isIn(one, all) {
        var isIn = false;
        angular.forEach(all, function (data, index, array) {
            if (data == one) {
                isIn = true;
            }
        });
        return isIn;
    }

    function bindCloseTabSpan() {
        $timeout(function () {
            $("button[name='closeTabBtn']").each(function () {
                $(this).on('click', function () {
                    $(this).closest("li").hide();
                    closeTab($(this).attr("id"));
                });
            });
        }, 300);
    }
    // 关闭某个tab
    function closeTab(nodeName) {
        angular.forEach($scope.contents, function (data, index, array) {
            if (nodeName == data.name) {
                $scope.contents.splice(index, 1);
                move()
            }
        });
    }

    // 首页的欢迎对象
    $scope.manager = $cookieStore.get("loginName");

    $scope.showStaff = false;
    $scope.showNotice = false;
    $scope.showTodo = false;
    function getPermission() {
        var res = {};
        //统一权限管理系统 获取指定系统指定用户的所有权限
        res.url = "permissionInfo/permissionListByUserName";
        var params = {"systemName":"君智OA", "systemSign":"zzoa", "targetId":"-1", "paging":{"pageSize":100,"pageNo":1}};
        var userName = $cookieStore.get("loginUserName");
        if (userName) {
            params.userName = userName;
        }
        res.js = JSON.stringify(params);
        $http.post("rest/util/link", res).success(function (data) {
            if (data) {
                var response = JSON.parse(data);
                var permissions;
                if(response.data) {
                    permissions = response.data;
                }
                var permissionArr = [];
                angular.forEach(permissions, function (data, index) {
                    permissionArr.push(data.name);
                });
                //将权限列表定义为全局变量
                $rootScope.permissionArr = permissionArr;

                if (permissionArr.indexOf("查看通知") > -1) {
                    $scope.showNotice = true;
                }
                if($scope.showNotice != true){
                    $scope.wait={
                        "width":"100%"
                    };
                    $scope.pageShow=true;
                    $scope.becomeShow=true;
                    $scope.contractShow=true;
                    $scope.diplomaShow=true;
                    $scope.abnormalShow=false
                }
                if (permissionArr.indexOf("查看待办事项") > -1) {
                    $scope.showTodo = true;
                }

                //每个人都可以看得到
                $scope.treeDataCheck = [
                    { "name" : "考勤管理", "id" : "100","show": true, "isTop":true, "children" : [
                        { "name" : "出差管理", "id" : "101", "isTop":false, "url": "page/trip.html", "children" : [] },
                        { "name" : "外出管理", "id" : "102", "isTop":false, "url": "page/out.html", "children" : [] },
                        { "name" : "假期管理", "id" : "103", "isTop":false, "url": "page/vacation.html", "children" : [] },
                        { "name" : "加班管理", "id" : "104", "isTop":false, "url": "page/over.html", "children" : [] },
                        { "name" : "打卡管理", "id" : "105", "isTop":false, "url": "page/punch.html", "children" : [] },
                        { "name" : "下载管理", "id" : "106", "isTop":false, "url": "page/down.html", "children" : [] }
                    ]}
                ];

                //具备查看考勤的权限的人
                if (permissionArr.indexOf("考勤统计") > -1) {
                    $scope.treeDataCheck.push(
                        { "name" : "统计相关", "id" : "120","show": true, "isTop":true, "children" : [
                            { "name" : "考勤列表", "id" : "121", "isTop":false, "url": "page/attendanceManager.html", "children" : [] },
                            { "name" : "考勤统计", "id" : "122", "isTop":false, "url": "page/attenSummary.html", "children" : [] },
                            { "name" : "单项统计", "id" : "123", "isTop":false, "url": "page/attenSingleSummary.html", "children" : [] },
                            { "name" : "年假统计", "id" : "124", "isTop":false, "url": "page/attenAnnualSummary.html", "children" : [] },
                            { "name" : "调休统计", "id" : "125", "isTop":false, "url": "page/attenOffSummary.html", "children" : [] },
                            { "name" : "合并统计", "id" : "126", "isTop":false, "url": "page/merge.html", "children" : [] }
                        ]}
                    );
                } else {
                    $scope.treeDataCheck.push(
                        { "name" : "统计相关", "id" : "120","show": true, "isTop":true, "children" : [
                            { "name" : "考勤列表", "id" : "121", "isTop":false, "url": "page/attendanceManager.html", "children" : [] }
                        ]}
                    );
                }

                if (permissionArr.indexOf("查看组管理") > -1) {
                    $scope.treeDataCheck.push(
                        { "name" : "组管理", "id" : "140","show": true, "isTop":false, "url": "page/group.html", "children" : [] }
                    );
                }

                if (permissionArr.indexOf("流程配置") > -1) {
                    $scope.treeDataCheck.push(
                        { "name" : "流程配置", "id" : "150","show": true, "isTop":false, "url": "page/process.html", "children" : [] }
                    );
                }

                if (permissionArr.indexOf("查看字典项管理") > -1) {
                    $scope.treeDataCheck.push(
                        { "name" : "字典项管理", "id" : "160","show": true, "isTop":false, "url": "page/dic.html", "children" : [] }
                    );
                }

                if (permissionArr.indexOf("查看资源管理") > -1) {
                    $scope.treeDataCheck.push(
                        { "name" : "资源池管理", "id" : "170","show": true, "isTop":false, "url": "page/resource.html", "children" : [] }
                    );
                }

                if (permissionArr.indexOf("人事管理") > -1) {
                    $scope.treeData.push(
                        { "name" : "人事管理","show": true, "id" : "200", "isTop":true, "children" : [
                            { "name" : "员工档案", "id" : "201", "isTop":false, "url": "page/staff.html", "children" : [] },
                            { "name" : "部门管理", "id" : "203", "isTop":false, "url": "page/departManage.html", "children" : [] },
                            { "name" : "角色管理", "id" : "202", "isTop":false, "url": "page/userGroup.html", "children" : [] },
                            { "name" : "异动管理", "id" : "204", "isTop":false, "url": "page/turnover.html", "children" : [] },
                            { "name" : "资质管理", "id" : "207", "isTop":false, "url": "page/Aptitude.html", "children" : [] },
                            { "name" : "考勤汇总", "id" : "205", "isTop":false, "url": "page/manage.html", "children" : [] },
                            { "name" : "人数计算", "id" : "206", "isTop":false, "url": "page/peopleCount.html", "children" : [] },
                            { "name" : "考勤白名单", "id" : "208", "isTop":false, "url": "page/whiteList.html", "children" : [] }
                        ]}
                    );
                } else if (permissionArr.indexOf("查看考勤汇总") > -1) {
                    $scope.treeData.push(
                        { "name" : "人事管理","show": true, "id" : "200", "isTop":true, "children" : [
                            { "name" : "考勤汇总", "id" : "205", "isTop":false, "url": "page/manage.html", "children" : [] }
                        ]}
                    );
                }

                if (permissionArr.indexOf("人才管理") > -1) {
                    $scope.treeData.push(
                        { "name" : "人才管理", "show": true,"id" : "220", "isTop":false, "url": "page/talent.html", "children" : [] }
                    );
                }

                if (permissionArr.indexOf("系统设置") > -1) {
                    $scope.treeData.push(
                        { "name" : "系统设置","show": true, "id" : "230", "isTop":true, "children" : [
                            { "name" : "备份还原", "id" : "231", "isTop":false, "url": "", "children" : [] },
                            { "name" : "日历管理", "id" : "232", "isTop":false, "url": "page/calendar.html", "children" : [] }
                        ]}
                    );
                }

                if (permissionArr.indexOf("商务财务") > -1) {
                    $scope.treeCommerce.push(
                        {"name" : "商务财务","show": true, "id" : "290", "isTop":true, "children" :[
                            { "name" : "供应商管理", "id" : "300", "isTop":false, "url": "page/provider.html", "children" : [] },
                            { "name" : "付款管理", "id" : "320", "isTop":false, "url": "page/payment.html", "children" : [] }
                        ]}

                    );
                }

                // if (permissionArr.indexOf("财务管理") > -1) {
                //     $scope.treeCommerce.push(
                //         { "name" : "付款管理", "id" : "320", "isTop":false, "url": "page/payment.html", "children" : [] }
                //     );
                // }

                //如果人事没有任何一个菜单权限 则隐藏上级权限菜单
                if ($scope.treeData.length > 0) {
                    //初始化时左侧默认展开 只展开第一个
                    /*$scope.expandedNodes = [$scope.treeData[0],$scope.treeData[1],$scope.treeData[2]];*/
                    $scope.expandedNodes = [$scope.treeData[0]];
                    if ($scope.treeData[0].children.length > 0) {
                        $scope.activeIndex = $scope.treeData[0].children[0].id;
                    }

                } else {
                    $scope.treeData[0] = { "name" : "无权限"};
                }

                //如果商务财务没有任何一个菜单权限 则隐藏上级权限菜单
                if ($scope.treeCommerce.length > 0) {
                    $scope.expandedNodesCommerce=[$scope.treeCommerce[0], $scope.treeCommerce[1]];
                } else {
                    $scope.treeCommerce[0] = { "name" : "无权限"};
                }

                //考勤初始化展开
                $scope.expandedNodesCheck=[$scope.treeDataCheck[0], $scope.treeDataCheck[1]];

                //默认选中第一个分类 即主页
                if ($scope.treeData1 != null) {
                    if ($scope.treeData1[0].children != null) {
                        $scope.nodeSelect($scope.treeData1[0].children[0]);
                    }
                }
            }
        }).error(function (data) {
            showOkModal("提示", "统一权限管理系统接口错误,请核实!");
        });
    }


    $(function () {
        //标签关闭按钮初始化
        bindCloseTabSpan();
        getPermission();
    });

    var idx=0;
    function move() {
        var num=$scope.contents.length;
        var len=document.getElementById("style2").clientWidth;
        var integer=parseInt(len/105);
        var allNum=num-integer;
        var endWidth=num*105+'px';
        $scope.moveStyle={
            "width":endWidth,
            "height":"44px"
        };
        var leftGo=$scope.left=function () {
            idx--;
            if(idx<0){
                idx=0
            }
            $scope.moveStyle.transform="translateX("+(-105*idx)+"px)";
        };
        var rightGo=$scope.right=function () {
            idx++;
            if(idx>allNum){
                idx=allNum
            }
            $scope.moveStyle.transform="translateX("+(-105*idx)+"px)";
        };
        if(num>integer){
            $scope.leftRight=true;
            rightGo()
        }
        if(num<=integer){
            $scope.leftRight=false;
            leftGo()
        }
    }
}]);