'use strict';

//系统管理模块
var roleModule = angular.module("roleModule", []);

var roleListController = function($scope, $http, $cookieStore) {
    $scope.req = {};
    $scope.roleReq = {};
    $scope.req.paging = {};
    $scope.req.paging.pageNo = 1;
    $scope.req.paging.pageSize = 15;
    $scope.totalRecordCount = 0;
    $scope.totalPage = 0;
    $scope.currentPage = 1;
    $scope.req.systemId=-1;
    $scope.times = 0;
    $scope.req.authorityName = '查看角色';
    $scope.authorityNameArra=[];//权限名称
    $scope.allDataSourceArra=[];//所有数据源
    $scope.title;//0创建,1编辑
    var systemIdArra=[];
    $http.post("rest/systemInfo/systemList", $scope.req).success(function (data) {
        if (data != null && data != undefined) {
            $scope.systemList = data.data;
            //获取数据源
            $.each($scope.systemList,function(index, systemInfo){
                var dataSource = systemInfo.dataSource;

                if(dataSource != null){
                    var systemId = systemInfo.id;
                    systemIdArra.push(systemId);//IdList用来左边导航定位用
                    var dataSourceInfo = {};
                    dataSourceInfo.systemId = systemId;
                    dataSourceInfo.dataSourceArra = [];
                    if(dataSource.indexOf('√') >= 0){
                        var dataSourceArraTemp = dataSource.split('√');
                        $.each(dataSourceArraTemp,function(index, dataSourceTemp){
                            var tableName = dataSourceTemp.split(';')[0];
                            var dataSourceInfoTemp={};
                            dataSourceInfoTemp.tableName = tableName;
                            dataSourceInfoTemp.source = dataSourceTemp;
                            dataSourceInfo.dataSourceArra.push(dataSourceInfoTemp);
                        });
                    }else{
                        var tableName = dataSource.split(';')[0];
                        var dataSourceInfoTemp={};
                        dataSourceInfoTemp.tableName = tableName;
                        dataSourceInfoTemp.source = dataSource;
                        dataSourceInfo.dataSourceArra.push(dataSourceInfoTemp);
                    }
                    $scope.allDataSourceArra.push(dataSourceInfo);
                }
            });
            $scope.req.systemId = $scope.systemList[0].id;
            $scope.changeRoleListBySystemId($scope.req.systemId);
            //$("#systemUl").find("li:eq(0)").addClass("liSelected");
        }
    }).error(function (data) {
        $("#serverErrorModal").modal({show: true});
    });

    //根据系统ID获取角色信息列表
    $scope.changeRoleListBySystemId = function(id) {
        $scope.req.paging.pageNo = 1;
        $scope.authorityNameArra=[];
        $scope.req.systemId=id;
        //$scope.roleReq.systemId = id;
        //增删改查赋予权限
        $scope.addFlag = false;
        $scope.deleteFlag = false;
        $scope.updateFlag = false;
        $scope.showFlag = false;

        $http.post("rest/permissionInfo/getAuthorityName", $scope.req).success(function (data) {
            if (data != null && data != undefined) {
                //判断是否为超级管理员，isAdmin为超级管理员具有所有权限，否则权限通过authorityNameArra匹配
                if(data.message != null && data.message=="isAdmin"){
                    $scope.addFlag = true;
                    $scope.deleteFlag = true;
                    $scope.updateFlag = true;
                    $scope.showFlag = true;
                }else{
                    $scope.authorityNameArra = data.data;
                    if($scope.authorityNameArra.indexOf('添加角色') !=-1 ){
                        $scope.addFlag = true;
                    }
                    if($scope.authorityNameArra.indexOf('删除角色') !=-1 ){
                        $scope.deleteFlag = true;
                    }
                    if($scope.authorityNameArra.indexOf('更新角色') !=-1 ){
                        $scope.updateFlag = true;
                    }
                    if($scope.authorityNameArra.indexOf('查看角色') !=-1 ){
                        $scope.showFlag = true;
                    }
                }
            }
            $scope.getRoleListBySystemId(id);
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };
    //根据系统ID获取角色信息列表
    $scope.getRoleListBySystemId = function(id) {
        $scope.req.systemId=id;
        $http.post("rest/roleInfo/list", $scope.req).success(function (data) {
            if (data != null && data != undefined) {
                $scope.roles = data.data;
                $scope.otherData = data.otherData;
                $scope.totalRecordCount = $scope.otherData[0];
                $scope.totalPage = $scope.otherData[1];

                //第一次进来默认选中第一个系统
                if ($scope.times == 0) {
                    $("#systemUl").find("li:eq(0)").addClass("liSelected");
                }
                $scope.times +=1;
            }
            $scope.setPageButtonStatus();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    $scope.authorityReq = {};
    $scope.authorityReq.paging = {};
    $scope.authorityReq.paging.pageNo = 1;
    $scope.authorityReq.paging.pageSize = 7;
    $scope.authroityTotalCount = 0;
    $scope.authroityTotalPage = 0;
    $scope.authroityCurrentPage = 1;
    //根据角色ID获取角色权限信息列表
    $scope.getAuthorityListByRoleId = function(id) {
        $scope.authorityReq.roleId = id;
        $http.post("rest/roleInfo/authorityList",$scope.authorityReq).success(function (data) {
            if (data != null && data != undefined) {
                $scope.authorityList = data.data;
                $scope.otherData = data.otherData;
                $scope.authroityTotalCount = $scope.otherData[0];
                $scope.authroityTotalPage = $scope.otherData[1];
                $("#showRoleModal").modal({show: true});
            }
            $scope.setPageButtonStatus1();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };
    $scope.systemList = null;
    //显示角色添加模态框
    $scope.showCreateRoleModal = function(){
        $scope.title="0";
        $scope.roleReq = {};
        $scope.roleReq.systemId = $scope.req.systemId;//绑定选中的systemId
        $scope.dataSource=null;
        $scope.singleDataSourceArra=[];//绑定选中的系统Id的数据源
        $.each($scope.allDataSourceArra,function(index,dataSourceInfo){
            if(dataSourceInfo.systemId == $scope.req.systemId){
                $scope.singleDataSourceArra = dataSourceInfo.dataSourceArra;
            }
        });
        $scope.getPermissionListBySystemId($scope.req.systemId);//绑定选中的系统Id的权限
        $scope.selected = [];
        $scope.selectedOutDatas=[];
        $scope.selectedPermissions=[];
        $scope.selectedNodes=[];//清空选择
        $scope.selectedPermissionShows=[];//清空已选权限显示
        $scope.permissionList = null;
        $scope.outDataList = null;
        $("#createRoleModal").modal({show: true});
        $("#tree").treeview();//清空树
    };
    //根据系统Id获取权限信息列表不分页
    $scope.getPermissionListBySystemId = function(id) {
        $scope.permissionReq=$scope.req;
        $scope.permissionReq.systemId = id;
        $scope.permissionReq.paging.pageSize = 10000;
        $http.post("rest/permissionInfo/list",$scope.permissionReq).success(function (data) {
            if (data != null && data != undefined) {
                $scope.permissionList = null;
                $scope.permissionList = data.data;

                //$scope.getOutDataListBySystemId(id);
                //$scope.radioValue = $scope.permissionList[0];//默认选中第一个
                //$scope.selectedPermission = $scope.permissionList[0];
            }else{
                $scope.permissionList = null;
            }
            $scope.req.paging.pageSize = 15;
        }).error(function (data) {
            $scope.req.paging.pageSize = 15;
            $("#serverErrorModal").modal({show: true});
        });
    };
    $scope.selectedNodes=[];
    $scope.selectedPermissionShows=[];
    $scope.getNodes=function(){
        $.each($scope.selectedPermissionShows,function(index, selectedPermissionShow){
            if(selectedPermissionShow != undefined && selectedPermissionShow.id == $scope.selectedPermission.id){
                $scope.selectedPermissionShows.splice(index,1);
            }
        });
        if($scope.selectedPermission.relation_flag == 0){
            var permissionInfo = {
                'id':$scope.selectedPermission.id,
                'name':$scope.selectedPermission.name,
                'targetId':0,
                'targetName':''
            };
            if(!$scope.chkPermiss(permissionInfo)){
                $scope.selectedPermissions.push(permissionInfo);
            }
            var selectedPermissionShow = {
                id:$scope.selectedPermission.id,
                name:$scope.selectedPermission.name,
                count:0,
                selectedPermission:permissionInfo
            };
            $scope.selectedPermissionShows.push(selectedPermissionShow);
        }else{
            var nodes = $("#tree").getTSNs(true);//获取树的全选和半选
            var selectedNodeInfo = {};
            selectedNodeInfo.permissionId = $scope.selectedPermission.id;
            selectedNodeInfo.nodes = [];
            selectedNodeInfo.nodes = nodes;
            $scope.selectedNodes.push(selectedNodeInfo);
            //统一权限下绑定的数据源重新绑定
            $scope.selectedPermissionsTemp = [];
            $.each($scope.selectedPermissions,function(index, permission){
                if(permission != undefined && permission.id != $scope.selectedPermission.id){
                    $scope.selectedPermissionsTemp.push(permission);
                }
            });
            $scope.selectedPermissions=$scope.selectedPermissionsTemp;
            $.each(nodes,function(index,node){
                $scope.updateSelected('add',node.value);
            });
            if(nodes != null && nodes.length > 0){
                var selectedPermissionShow = {
                    id:$scope.selectedPermission.id,
                    name:$scope.selectedPermission.name,
                    count:nodes.length
                };
                $scope.selectedPermissionShows.push(selectedPermissionShow);
            }

        }
    };


    $scope.getCheckstate=function(nodeId){
        var checkstate = 0;
        $.each($scope.selectedNodes,function(index,selectedNode){
            if($scope.selectedPermission!=undefined && $scope.selectedPermission.id == selectedNode.permissionId){
                $.each(selectedNode.nodes,function(index,selectedNode){
                    if(nodeId == selectedNode.id){
                        checkstate = selectedNode.checkstate;
                        return true;
                    }
                });
                return true;
            }
        });
        return checkstate;
    };
    $scope.treenodes = {};//加载后的树的数据
    function loadTree() {
        var o = { showcheck: true,
        };
        o.data = topNodes;
        $("#tree").treeview(o);
        $scope.treenodes = o.data;
    }
    function addNodes(targetNode) {
        $.each(subNodes, function (index2, subNode) {
            if (subNode.parentId == targetNode.id) {
                if (!targetNode.ChildNodes) {
                    targetNode.ChildNodes = [];
                }
                var nodeValue={
                    id:subNode.id,
                    name:subNode.name,
                    dataSource:subNode.dataSource,
                };
                var subNodeTemp = {
                    id:subNode.id,
                    text:subNode.name,
                    value:nodeValue,
                    showcheck:true,
                    complete:true,
                    isexpand:true,
                    checkstate:$scope.getCheckstate(subNode.id),
                    hasChildren:parentIdMap[subNode.id]
                };
                targetNode.ChildNodes.push(subNodeTemp);
                addNodes(subNodeTemp);
            }
        });
    }
    var topNodes = [];
    var subNodes = [];
    $scope.buildeTree=function(list){
        topNodes = [];//初始化
        subNodes = [];//初始化
        $.each(list, function (index, topNode) {
            if(parentIdMap[topNode.parentId]) {
                parentIdMap[topNode.parentId] +=1;
            }else{
                parentIdMap[topNode.parentId] = 1;
            }

            if (topNode.parentId == 0) {
                var nodeValue={
                    id:topNode.id,
                    name:topNode.name,
                    dataSource:topNode.dataSource,
                };
                var topNodeTemp = {
                    id:topNode.id,
                    text:topNode.name,
                    value:nodeValue,
                    showcheck:true,
                    complete:true,
                    isexpand:true,
                    checkstate:$scope.getCheckstate(topNode.id),
                    hasChildren:parentIdMap[topNode.id]
                };
                topNodes.push(topNodeTemp);
            } else {
                subNodes.push(topNode);
            }
        });
        $.each(topNodes, function (index, topNode) {
            $.each(subNodes, function (index2, subNode) {
                if (subNode.parentId == topNode.id) {
                    if (!topNode.ChildNodes) {
                        topNode.ChildNodes = [];
                    }
                    var nodeValue={
                        id:subNode.id,
                        name:subNode.name,
                        dataSource:subNode.dataSource,
                    };
                    var subNodeTemp = {
                        id:subNode.id,
                        text:subNode.name,
                        value:nodeValue,
                        showcheck:true,
                        complete:true,
                        isexpand:true,
                        checkstate:$scope.getCheckstate(subNode.id),
                        hasChildren:parentIdMap[subNode.id]
                    };
                    topNode.ChildNodes.push(subNodeTemp);
                    addNodes(subNodeTemp);
                }

            });
        });
    }

    var parentIdMap={};
    //根据系统Id获取关联信息列表
    $scope.getOutDataListBySystemId = function(id) {
        $scope.outDataReq=$scope.req;
        $scope.outDataReq.systemId=id;
        $scope.outDataReq.dataSource = $scope.dataSource;
        $http.post("rest/roleInfo/outDataList",$scope.outDataReq).success(function (data) {
            if (data != null && data != undefined) {
                $scope.outDataList = data.data;
                $scope.buildeTree($scope.outDataList);//创建树
                loadTree();//加载树结构
            }else{
                $scope.outDataList = null;
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    // 根据系统ID改变关联信息和权限信息
    $scope.changeInfoBySystemId = function(id){
        $scope.selected = [];//清空选择
        $scope.selectedOutDatas=[];//清空选择
        $scope.selectedPermissions=[];//清空选择
        $scope.permissionList = null;
        $scope.outDataList = null;
        $scope.getPermissionListBySystemId(id);
        $scope.singleDataSourceArra=[];
        $.each($scope.allDataSourceArra,function(index,dataSourceInfo){
            if(dataSourceInfo.systemId == id){
                $scope.singleDataSourceArra = dataSourceInfo.dataSourceArra;
            }
        });
    };
    var targetIdList=[];
    //根据系统Id获取关联信息列表
    $scope.getTargetIdList = function(permissionId) {
        $scope.outDataReq=$scope.req;
        $scope.outDataReq.systemId=$scope.roleReq.systemId;
        $scope.outDataReq.permissionId=permissionId;
        $scope.outDataReq.dataSource = $scope.dataSource;
        $http.post("rest/roleInfo/targetIdList",$scope.outDataReq).success(function (data) {
            if (data != null && data != undefined) {
                targetIdList = data.data;
                $scope.outDataList = [];
                $.each($scope.outDataListTemp,function(index,outData){
                    if(targetIdList.indexOf(Number(outData.id))>-1){
                        $scope.outDataList.push(outData);
                    }
                });
                $scope.buildeTree($scope.outDataList);//创建树
                loadTree();//加载树结构
            }else{
                targetIdList = null;
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    //切换关联表数据
    $scope.selectedPermission = null;
    $scope.changeOutDataList = function(object){
        $scope.selectedPermission = object;
        if(object.relation_flag==1){
            //重新获取外部关联数据，判断是否为超级管理员，超级管理员时不需要过滤
            if($cookieStore.get("loginUserName")=='admin'){
                $scope.getOutDataListBySystemId($scope.roleReq.systemId);
            }else{
                //重新获取外部关联数据
                $scope.getTargetIdList(object.id);
            }
        }else{
            $("#tree").treeview();//清空树
        }
    };

    // 判断是否已经选择
    $scope.chkPermiss = function(permissionInfo){
        var flag = false;
        $.each($scope.selectedPermissions,function(index,selectedPermission){
            if(selectedPermission.id == permissionInfo.id && selectedPermission.targetId == permissionInfo.targetId){
                flag = true;
                return true;
            }
        });
        return flag;
    };

    //更新选中，添加和移除
    $scope.selected = [];
    $scope.selectedOutDatas=[];
    $scope.selectedPermissions=[];
    $scope.updateSelected=function(action,object){
        if(action == 'add' && $scope.selected.indexOf(object) == -1){
            $scope.selected.push(object.id);
            $scope.selectedOutDatas.push(object);
            var permissionInfo = {
                'dataSource':$scope.dataSource,
                'id':$scope.selectedPermission.id,
                'name':$scope.selectedPermission.name,
                'targetId':object.id,
                'targetName':object.name,
                'targetTableName':$scope.dataSource
            };
            if(!$scope.chkPermiss(permissionInfo)){
                $scope.selectedPermissions.push(permissionInfo);
            }
        }
        if(action == 'remove' && $scope.selected.indexOf(object.id) != -1){
            var idx = $scope.selected.indexOf(object.id);
            $scope.selected.splice(idx,1);
            $scope.selectedOutDatas.splice(idx,1);
            var selectedPermissionsTemp = $scope.selectedPermissions;
            $.each(selectedPermissionsTemp,function(index, permission){
                if(permission != undefined && permission.targetId == object.id && permission.dataSource == $scope.dataSource){
                    $scope.selectedPermissions.splice(index,1);
                }
            });
        }
    };

    //删除添加的权限
    $scope.deleteSelected=function(object){
        //删除选中贡献显示用
        $.each($scope.selectedPermissionShows,function(index, selectedPermissionShow){
            if(selectedPermissionShow != undefined && selectedPermissionShow.id == object.id){
                $scope.selectedPermissionShows.splice(index,1);
            }
        });
        //删除选中贡献后台存储用
        if($scope.selectedPermissions.indexOf(object.selectedPermission) != -1){
            var index = $scope.selectedPermissions.indexOf(object.selectedPermission);
            $scope.selectedPermissions.splice(index,1);
        }
    };

    //更新选中状态
    $scope.updateSelection = function($event,object){
        var checkbox = $event.target;
        var action = (checkbox.checked?'add':'remove');
        $scope.updateSelected(action,object);
    };

    // 记住选中状态
    $scope.isSelected = function(outData){
        var flag = false;
        $.each($scope.selectedPermissions,function(index, permission){
            var targetFlag = permission != undefined && permission.targetId == outData.id;
            var permissionFlag = $scope.selectedPermission != null && permission.id == $scope.selectedPermission.id;
            if(targetFlag && permissionFlag){
                flag = true;
                return true;
            }
        });
        return flag;
    };

    //创建角色
    $scope.createRoleInfo = function() {
        $scope.roleReq.permissionInfoList=$scope.selectedPermissions;
        $http.post("rest/roleInfo/insert", $scope.roleReq).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', "创建成功！");
                    var index = $.inArray($scope.req.systemId,systemIdArra);
                    $("#systemUl").find("li:eq(index)").addClass("liSelected");
                    $scope.getRoleListBySystemId($scope.req.systemId);//刷新列表
                } else {
                    $("#serverErrorModal").modal({show: true});
                }
            }
        }).error(function (data) {
            showOkModal('提示', data.message);
            $("#serverErrorModal").modal({show: true});
        });
    };

    //显示角色信息编辑窗口
    $scope.showEditModal = function(index) {
        $scope.title="1";
        var roleInfo = $scope.roles[index];
        $scope.selected=[];
        $scope.getPermissionListBySystemId(roleInfo.systemId);
        $scope.roleReq = {};
        $scope.roleReq.id = roleInfo.id;
        $scope.roleReq.systemId = roleInfo.systemId;
        $scope.roleReq.name = roleInfo.name;
        $scope.roleReq.sign = roleInfo.sign;
        $scope.roleReq.remark = roleInfo.remark;
        $scope.authorityReq.paging.pageSize = 10000;
        $scope.authorityReq.roleId = roleInfo.id;
        $scope.outDataList = null;
        $scope.singleDataSourceArra=[];//绑定选中的系统Id的数据源
        $scope.selectedPermissionShows=[];//清空数据源
        if($scope.allDataSourceArra.length > 0){
            $.each($scope.allDataSourceArra,function(index,dataSourceInfo){
                if(dataSourceInfo.systemId == $scope.req.systemId){
                    $scope.singleDataSourceArra = dataSourceInfo.dataSourceArra;//绑定数据源
                }
            });
            $scope.dataSource=$scope.singleDataSourceArra[0].source;
        }
        $scope.getOutDataListBySystemId($scope.roleReq.systemId);
        $http.post("rest/roleInfo/authorityList",$scope.authorityReq).success(function (data) {
            if (data != null && data != undefined) {
                var authorityList = data.data;
                var countMap={};
                var nodeMap = {};
                var autorityListTempArra = [];
                if(authorityList && authorityList.length > 0){
                    $.each(data.data,function(index1,authority) {
                        if($scope.outDataList && $scope.outDataList.length > 0){
                            $.each($scope.outDataList,function(index,outData) {
                                if(authority.targetId == outData.id){
                                    authority.parentId = outData.parentId;
                                    autorityListTempArra.push(authority);
                                }
                            });
                        }

                    });
                }

                var autorityParentIdMap={};
                $.each(autorityListTempArra, function (index, autorityListTemp) {
                    if (autorityParentIdMap[autorityListTemp.parentId]) {
                        autorityParentIdMap[autorityListTemp.parentId] += 1;
                    } else {
                        autorityParentIdMap[autorityListTemp.parentId] = 1;
                    }
                });
                $.each(authorityList,function(index,authority) {
                    if(autorityParentIdMap[authority.id] == 0){
                        nodeclick(authority.id);
                    }
                });



                /*var s = $scope.treenodes;
                nodeclick(56400977);*/
                $.each(authorityList,function(index,authority){
                    var node = {
                        id:authority.targetId,
                        name:authority.targetName,
                        checkstate:1
                    };
                    if(countMap[authority.id]) {
                        countMap[authority.id] +=1;
                        nodeMap[authority.id].push(node);
                    }else{
                        nodeMap[authority.id] = [];
                        nodeMap[authority.id].push(node);
                        countMap[authority.id] = 1;
                    }

                });
                var id = 0;
                $.each(authorityList,function(index,authority){
                    var permissionInfo = {
                        'id':authority.id,
                        'name':authority.name,
                        'targetId':authority.targetId,
                        'targetName':authority.targetName
                    };
                    if(!$scope.chkPermiss(permissionInfo)){
                        $scope.selectedPermissions.push(permissionInfo);
                        $scope.selected.push(permissionInfo.targetId);
                    }
                    if(authority.targetId == 0){
                        var selectedPermissionShow = {
                            id:authority.id,
                            name:authority.name,
                            count:0,
                            selectedPermission:permissionInfo
                        };
                        $scope.selectedPermissionShows.push(selectedPermissionShow);
                    }else{
                        if(id != authority.id){
                            var selectedPermissionShow = {
                                id:authority.id,
                                name:authority.name,
                                count:countMap[authority.id],
                                //selectedPermission:permissionInfo
                            };
                            $scope.selectedPermissionShows.push(selectedPermissionShow);
                            var selectedNodeInfo = {};
                            selectedNodeInfo.permissionId = authority.id;
                            selectedNodeInfo.nodes = [];
                            selectedNodeInfo.nodes = nodeMap[authority.id];
                            $scope.selectedNodes.push(selectedNodeInfo);
                            id = authority.id;
                        }
                    }
                });
            }
            $("#createRoleModal").modal({show: true});
        }).error(function (data) {
        });
    };
    $scope.item;
    function nodeclick(id) {
        $scope.item = getItem(id);
        cascade(check, $scope.item, 1);
        bubble(check, $scope.item, 1);
    }
    function getItem(id) {
        var t;
        $.each($scope.treenodes,function(index, treeNode){
            if(treeNode.id == id){
                t = treeNode;
            }else{
                var childNodes = treeNode.childNodes;
                if(treeNode.childNodes){

                }
            }

        });
        return t;
    }
    function cascade(fn, item, args) {
        if (fn(item, args, 1) != false) {
            if (item.ChildNodes != null && item.ChildNodes.length > 0) {
                var cs = item.ChildNodes;
                for (var i = 0, len = cs.length; i < len; i++) {
                    cascade(fn, cs[i], args);
                }
            }
        }
    }
    //bubble to parent
    function bubble(fn, item, args) {
        var p = item.parent;
        while (p) {
            if (fn(p, args, 0) === false) {
                break;
            }
            p = p.parent;
        }
    }
    function check(item, state, type) {
        if (type == 1) {
            item.checkstate = state;
        }
        else {// go to childnodes
            var cs = item.ChildNodes;
            var l = cs.length;
            var ch = true;
            for (var i = 0; i < l; i++) {
                if ((state == 1 && cs[i].checkstate != 1) || state == 0 && cs[i].checkstate != 0) {
                    ch = false;
                    break;
                }
            }
            if (ch) {
                item.checkstate = state;
            }
            else {
                item.checkstate = 2;
            }
        }
    }
    //编辑系统信息
    $scope.editRoleInfo = function() {
        $scope.roleReq.permissionInfoList = $scope.selectedPermissions;
        $http.put("rest/roleInfo/update", $scope.roleReq).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    showOkModal('提示', "修改成功！");
                    $scope.getRoleListBySystemId($scope.roleReq.systemId);//刷新列表
                } else {
                    $("#serverErrorModal").modal({show: true});
                }
            }
            $scope.setPageButtonStatus();
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    //显示删除确认模态框
    $scope.showDelConfirmModal = function(index) {
        showConfirmModal('您确定删除吗？', $scope.deleteRole);
        $scope.deleteSystemId = $scope.roles[index].id;
    };

    //删除角色信息
    $scope.deleteRole = function () {
        $http.delete("rest/roleInfo/"+$scope.deleteSystemId+"/delete", null).success(function (data) {
            showOkModal('提示', data.resultCode);
            if(data.resultCode=="RESULT_SUCCESS") {
                showOkModal('提示', "删除成功！");
                $scope.getRoleListBySystemId($scope.req.systemId);//刷新列表
            } else {
                $("#serverErrorModal").modal({show: true});
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };

    //设置分页组件按钮的状态
    $scope.setPageButtonStatus = function() {
        if($scope.totalPage > $scope.req.paging.pageNo) {
            $("#nextPageLink").removeClass("disabled");
            $("#nextPage").attr("unable", false);
        } else {
            $("#nextPageLink").addClass("disabled");
            $("#nextPage").attr("unable", true);
        }
        if($scope.req.paging.pageNo == 1) {
            $("#previousPageLink").addClass("disabled");
            $("#previousPage").attr("unable", true);
        } else {
            $("#previousPageLink").removeClass("disabled");
            $("#previousPage").attr("unable", false);
        }
    };

    //上一页
    $scope.authorityPreviousPage = function () {
        if($("#previousPage1").attr('unable') == "false") {
            $scope.authorityReq.paging.pageNo = $scope.authorityReq.paging.pageNo - 1;
            $scope.getAuthorityListByRoleId($scope.authorityReq.roleId);
        }
    };

    //下一页
    $scope.authorityNextPage = function () {
        if ($("#nextPage1").attr('unable') == "false") {
            $scope.authorityReq.paging.pageNo = $scope.authorityReq.paging.pageNo + 1;
            $scope.getAuthorityListByRoleId($scope.authorityReq.roleId);
        }
    };

    //设置分页组件按钮的状态
    $scope.setPageButtonStatus1 = function() {
        if($scope.authroityTotalPage > $scope.authorityReq.paging.pageNo) {
            $("#nextPageLink1").removeClass("disabled");
            $("#nextPage1").attr("unable", false);
        } else {
            $("#nextPageLink1").addClass("disabled");
            $("#nextPage1").attr("unable", true);
        }
        if($scope.authorityReq.paging.pageNo == 1) {
            $("#previousPageLink1").addClass("disabled");
            $("#previousPage1").attr("unable", true);
        } else {
            $("#previousPageLink1").removeClass("disabled");
            $("#previousPage1").attr("unable", false);
        }
    };

    //上一页
    $scope.previousPage = function () {
        if($("#previousPage").attr('unable') == "false") {
            $scope.req.paging.pageNo = $scope.req.paging.pageNo - 1;
            $scope.getRoleListBySystemId($scope.req.systemId);
        }
    };

    //下一页
    $scope.nextPage = function () {
        if ($("#nextPage").attr('unable') == "false") {
            $scope.req.paging.pageNo = $scope.req.paging.pageNo + 1;
            $scope.getRoleListBySystemId($scope.req.systemId);
        }
    };


    var mu = document.getElementById("systemUl");
    var li = mu.getElementsByTagName("li");
    mu.onclick = function (e) {
        e = e||window.event;
        var target = e.srcElement || e.target;
        for (var i= 0,len=li.length;i<len;i++) {
            li[i].className = target ==li[i] ? "liSelected":"";
        }
    };

};


roleModule.$inject = ['$scope', '$http', '$cookieStore'];
roleModule.controller("roleListController", roleListController);
