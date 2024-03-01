'use strict';

indexModule.directive('ngRightClick', function($parse) {
    return function(scope, element, attrs) {
        var fn = $parse(attrs.ngRightClick);
        element.bind('contextmenu', function(event) {
            scope.$apply(function() {
                event.preventDefault();
                fn(scope, {$event:event});
            });
        });

    };
});

indexModule.controller('resourceController',['$scope', '$http', '$q','$location','$timeout','resourceService','$cookieStore','staffService', function ($scope, $http, $q,$location,$timeout,resourceService,$cookieStore,staffService) {
    //双击选中的目录
    $scope.clickCatalog = null;

    $scope.formData = {};

    $scope.staffId = $cookieStore.get("loginUserId");

    //流程定义
    $scope.processDefs = {};

    $scope.renameCatalog = null;

    //修改资源和新增资源区别
    $scope.noUpload = false;

    //控制权限按钮
    $scope.judgePermission = function () {
        var promise = staffService.getAllPermissions();
        promise.then(function (data) {
            if (data && data.length > 0) {
                if(data.indexOf("资源上传") > -1) {
                    $scope.canUpload = true;
                }
                if(data.indexOf("资源目录管理") > -1) {
                    $scope.canAddCata = true;
                }
                if(data.indexOf("资源删除") > -1) {
                    $scope.canRemoveResource = true;
                }
                if(data.indexOf("资源修改") > -1) {
                    $scope.canUpdateResource = true;
                }
                if(data.indexOf("资源移动") > -1) {
                    $scope.canMoveResource = true;
                }
            }

        });
    };
    $scope.judgePermission();

    //获取指定目录下的所有目录集合  根目录为-1
    $scope.getNowResources=function () {
        var promise = resourceService.getCatalogs(-1);
        promise.then(function (data) {
            if (data.resultCode === "RESULT_SUCCESS") {
                $scope.catalogs = data.data;
            }
        });
    };
    $scope.getNowResources(-1);

    //获取所有流程定义
    function getAllProcessDefinition() {
        $http.get("rest/activity/processDefinition").success(function (data) {
            if (data != null && data != undefined) {
                $scope.processDefs = data;
            }
        });
    }

    document.onclick=function () {
        $timeout(function () {
            $scope.rightDiv=false;
            return;
        },100);
    };

    $scope.catalog=function () {
        $scope.allFile=false;
        $scope.allName=[];
    };

    //第一层 右击
    $scope.rightClick=function (index) {
        $scope.clickResource = false;
        var e = event || window.event;
        var scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
        var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
        var x = e.pageX || e.clientX + scrollX;
        var y = e.pageY || e.clientY + scrollY;
        $scope.rightDiv=true;
        $scope.where={
            "left":x,
            "top":y,
        };
        // 重命名 第一层
        $scope.changeCatalog=function () {
            $scope.canEditC = true;
            $scope.rightDiv=false;
            $timeout(function () {
                //修改中的目录
                $scope.renameCatalog = $scope.catalogs[index];
                var element = document.getElementById("inputName"+$scope.catalogs[index].id);
                if (element){
                    element.focus();
                }
            });
        };
        // 第一层删除目录
        $scope.removeCatalog = function () {
            var catalogId = $scope.catalogs[index].id;
            showConfirmModal("确定要删除此目录吗？", function () {
                $http.delete("rest/catalog/" + catalogId +"/delete").success(function (data) {
                    if (data !== null && data !== undefined) {
                        if(data.resultCode === "RESULT_SUCCESS") {
                            //刷新页面
                            $scope.getNowResources(-1);
                            $scope.rightDiv = false;
                        } else {
                            showOkModal("提示", data.message);
                        }
                    }
                });
            });
            /*$scope.rightDiv=false;
            $scope.catalogs.splice(index,1)*/
        }
    };

    // 双击
    $scope.allName=[];

    $scope.doubleClick=function (index) {
        $timeout(function() {
            $scope.clickCatalog = $scope.catalogs[index];
            var name={
                "name":$scope.catalogs[index].name,
                "id":$scope.clickCatalog.id
            };
            $scope.allName.push(name);
            $scope.allFile=true;
        }, 500)
    };

    $scope.$on('transfer.type', function(event, data) {
        $scope.allName.push(data);
    });

    //修改资源信息 传递给父级
    $scope.$on('updateResource', function(event, data) {
        //修改时禁用 文件的选择框
        $scope.noUpload = true;
        $scope.formData = data;
        //初始化流程定义
        getAllProcessDefinition();
        $scope.type = "secret";
        if ($scope.formData.type === "public" || $scope.formData.type === "secret") {
            $scope.type = $scope.formData.type;
        } else {
            $scope.type = "choose";
            var groupArr = $scope.formData.type.split(",");
            angular.forEach($scope.groupSections, function (section) {
                if (groupArr.indexOf(section.groupId + "") > -1) {
                    section.ticked = true;
                }
            });
        }
    });


    $scope.catalogChange=function (index) {
        $timeout(function () {
            var myId=index+1;
            $scope.allName.splice(myId,$scope.allName.length);
            $scope.clickCatalog = $scope.allName[index];
            $scope.$broadcast('parentContent', $scope.allName[index]);
        },500)
    };

    //响应子controller双击
    $scope.$on('childDoubleClick', function(event, data) {
        $scope.clickCatalog = data;
    });

    //组的初始化
    $http.get("rest/group/list").success(function (data) {
        if (data !== null && data !== undefined) {
            $scope.groupSections = data.data;
        }
    });

    //多选框默认文本替换
    $scope.localLang = {
        nothingSelected : "请选择允许下载的组"
    };

    //监听类型选择
    $scope.$watch("type", function (val) {
        //选择了指定组公开
        $scope.needChooseGroup = "choose" === val;
        $scope.needChooseAction = ("secret" === val || "choose" === val);
    });

    //准备上传文件
    $scope.toUploadFile = function () {
        //上传时启用文件选择框
        $scope.noUpload = false;
        if (!$scope.clickCatalog) {
            showOkModal("提示", "根目录不能上传文件！");
            return;
        }
        //初始化流程定义
        getAllProcessDefinition();
        $scope.formData = {};
        $scope.type = "";
        angular.element("#uploadResourceModal").modal({show: true});
    };

    //文件框onchange事件
    $scope.fileNameChanged = function (ele) {
        //文件所属目录
        $scope.formData.belong = $scope.clickCatalog.id;
        var filePath = ele.value;
        var fileName = filePath.substr(filePath.lastIndexOf("\\") + 1);

        $scope.formData.name = fileName;
        $scope.formData.fileName = fileName;
        $scope.$apply();
    };

    //提交表单 然后上传文件
    $scope.submit = function () {
        //上传文件才需要校验
        if (!$scope.noUpload) {
            var files = document.getElementsByName('file')[0].files;
            if (files.length < 1) {
                showOkModal("提示信息", "请先选择资源文件！");
                return;
            }
        }
        if($scope.type) {
            if ($scope.type === 'secret') {
                if (!$scope.formData.actReId) {
                    showOkModal("提示", "请选择绑定的流程");
                    return;
                }
                $scope.formData.type = $scope.type;

            } else if ($scope.type === 'choose') {
                if ($scope.selectGroups.length < 1) {
                    showOkModal("提示", "请选择至少一个组");
                    return;
                }
                if (!$scope.formData.actReId) {
                    showOkModal("提示", "请选择绑定的流程");
                    return;
                }
                var groupArr = [];
                angular.forEach($scope.selectGroups, function (group) {
                    groupArr.push(group.groupId);
                });
                $scope.formData.type = groupArr.join(",");
            } else {
                $scope.formData.type = $scope.type;
            }
        } else {
            showOkModal("提示", "请选择文件的下载权限");
            return;
        }
        $scope.formData.isSecret = "0";
        $scope.formData.name = $scope.formData.fileName;
        if ($scope.formData.id) {
            //修改
            $http.put("rest/resource/updateRes", $scope.formData).success(function (data) {
                if (data !== null && data !== undefined) {
                    showOkModal('提示', data.message);
                    if (data.resultCode === "RESULT_SUCCESS") {
                        //刷新页面资源文件的显示
                        $scope.getResources();
                        angular.element("#uploadResourceModal").modal('hide');
                    }
                }
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
        } else {
            //新增
            $http.post("rest/resource/add", $scope.formData).success(function (data) {
                if (data !== null && data !== undefined) {
                    showOkModal('提示', data.message);
                    if (data.resultCode === "RESULT_SUCCESS") {
                        $scope.upload(data.obj);
                    }
                }
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
        }


    };

    //上传文件
    $scope.upload = function (resource) {
        var resourceId = resource.id;
        var files = document.getElementsByName('file')[0].files;
        if (files.length < 1) {
            showOkModal("提示信息", "请先选择文件！");
            return;
        }
        $http({
            method: 'POST',
            url: 'rest/resource/'+resourceId+'/upload',
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
            if (data.resultCode === "RESULT_SUCCESS") {

            } else {
                //上传失败
                showOkModal('提示', data.message);
                //todo 失败后的处理 删除成功的那条记录？
            }

            //刷新页面资源文件的显示
            $scope.getResources();
            angular.element("#uploadResourceModal").modal('hide');
        }).error(function (err, status) {
            console.log(err);
        });
    };

    //获取当前目录下的所有文件
    $scope.getResources = function () {
        var promise = resourceService.getResourceByCatalogId($scope.clickCatalog.id, $scope.staffId);
        promise.then(function (data) {
            if (data.resultCode === "RESULT_SUCCESS") {
                $scope.resources = data.data;
            }
        });
    };

    //新增
    $scope.newFile=function () {
        $scope.addFiles=true;
        $timeout(function () {
            var element = document.getElementById("addMyFile");
            if (element){
                element.focus();
                $scope.addName="新增文件夹";
            }
        });
    };

    $scope.add=function () {
        if(document.getElementById("addMyFile").value==""){
            document.getElementById("addMyFile").value="新增文件夹";
        }
        $scope.addName=document.getElementById("addMyFile").value;
        $scope.formData.name=$scope.addName;
        $scope.formData.pId="-1";
        $http.post("rest/catalog/catalogAdd", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    $scope.addFiles=false;
                    $scope.formData.name="";
                     $scope.getNowResources(-1);
                } else {
                    showOkModal('提示', data.message);
                    $scope.addFiles=false;
                }
            }
        });
    };

    //第一层文件夹 重命名
    $scope.rename = function () {
        $scope.canEditC = false;
        var newVal = document.getElementById("inputName"+$scope.renameCatalog.id).value;
        if (newVal === "" || newVal === null) {
            showOkModal("提示", "文件名非法!");
            //刷新页面
            $scope.getNowResources(-1);
            return;
        }
        $scope.renameCatalog.name = newVal;
        $http.put("rest/catalog/update", $scope.renameCatalog).success(function (data) {
            if (data !== null && data !== undefined) {
                if(data.resultCode === "RESULT_SUCCESS") {
                    //刷新页面
                    $scope.getNowResources(-1);
                } else {
                    showOkModal('提示', data.message);
                }
            }
        });
    };

    $scope.disSub=true;
    $scope.radioChange=function () {
        var chooseId = $scope.formData.processor;
        if(chooseId!==undefined){
            $scope.disSub=false;
        }
    };

    $scope.$on('downRequests', function(event, data) {
        $scope.formData.id=data;
        $scope.submitDown = function () {
            var staffId = $scope.formData.processor;
            var name = document.getElementById("down" + staffId).innerText;
            $http.post("rest/down/"+ staffId +"/submit", $scope.formData).success(function (data) {
                if (data !== null && data !== undefined) {
                    if(data.resultCode === "RESULT_SUCCESS") {
                        $("#DownChooseModal").modal('hide');
                        showOkModal('提示', "提交成功，下一审批人为&nbsp;&nbsp;&nbsp;"+name);
                    } else {
                        showOkModal('提示', data.message);
                    }
                }
            });
        };
    });

}]);



indexModule.controller('secondFloorController',['$scope', '$http', '$q','$location','$timeout','resourceService', function ($scope, $http, $q,$location,$timeout,resourceService) {

    //ztree实体
    var zTreeObj;

    //准备移动到的目录id
    $scope.movingCataId = null;

    //初始化zTree
    function zTreeInit() {
        var setting = {
            data: {
                simpleData:{
                    enable: true,
                    idKey: "id",
                    pIdKey: "pId",
                    rootPId: -1
                }
            },
            callback: {
                onClick: zTreeOnclick
            }
        };
        $http.get("rest/catalog/list").success(function (data) {
            if (data !== null && data !== undefined) {
                zTreeObj = $.fn.zTree.init($("#resourceTree"), setting, data.data);
                zTreeObj.expandAll(true);
            }
        });
    }

    //zTree点击事件
    function zTreeOnclick(e, treeId, treeNode) {
        $scope.movingCataId = treeNode.id;
    }

    //根据目录id获取其下目录
    $scope.getCatalogs = function (catalogId) {
        var promise = resourceService.getCatalogs(catalogId);
        promise.then(function (data) {
            if (data.resultCode === "RESULT_SUCCESS") {
                $scope.allCatalogs2 = data.data;
            }
        });
    };

    $scope.getCatalogs($scope.clickCatalog.id);


    document.onclick=function () {
        $timeout(function () {
            $scope.rightDiv=false;
            return;
        },100);
    };

    $scope.doubleClicks=function (index) {
        $timeout(function() {
            var name={
                "name":$scope.allCatalogs2[index].name,
                "id":$scope.allCatalogs2[index].id
            };
            $scope.$emit('transfer.type', name);
            //子controller中把双击的目录传递给父controller
            $scope.$emit('childDoubleClick', $scope.allCatalogs2[index]);

            $scope.getCatalogs($scope.allCatalogs2[index].id);
            $scope.getResources();
        }, 200)
    };

    $scope.$on('parentContent', function(event, data) {
        /*$scope.clickCatalog = data;*/
        $scope.getCatalogs($scope.clickCatalog.id);
        $scope.getResources();
    });

    $scope.getResources();

    // 新增
    $scope.add = function () {
        if(document.getElementById("addMyFile").value==""){
            document.getElementById("addMyFile").value="新增文件夹";
        }
        $scope.addName=document.getElementById("addMyFile").value;
        $scope.formData.name=$scope.addName;
        $scope.formData.pId=$scope.clickCatalog.id;
        //return;
        $http.post("rest/catalog/catalogAdd", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    $scope.addFiles=false;
                    $scope.formData.name="";
                    $scope.getCatalogs($scope.clickCatalog.id);
                } else {
                showOkModal('提示', data.message);
                    $scope.addFiles=false;
                }
            }
        });
    };

    //目录的右击
    $scope.rightClickTwo=function (index) {
        $scope.clickResource = false;
        var e = event || window.event;
        var scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
        var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
        var x = e.pageX || e.clientX + scrollX;
        var y = e.pageY || e.clientY + scrollY;
        $scope.rightDiv=true;
        $scope.canDown = false;
        $scope.where={
            "left":x-250,
            "top":y-159,
        };
        // 第二层 重命名
        $scope.changeCatalog = function () {
            $scope.childcanEditC = true;
            $scope.rightDiv = false;
            $scope.canEditC = true;
            $scope.renameCatalog = $scope.allCatalogs2[index];
            $timeout(function () {
                var element = document.getElementById("inputName"+$scope.allCatalogs2[index].id);
                if (element){
                    element.focus();
                }
            });
        };
        // 目录删除 第二层
        $scope.removeCatalog = function () {
            var catalogId = $scope.allCatalogs2[index].id;
            showConfirmModal("确定要删除此目录吗？", function () {
                $http.delete("rest/catalog/" + catalogId +"/delete").success(function (data) {
                    if (data !== null && data !== undefined) {
                        if(data.resultCode === "RESULT_SUCCESS") {
                            //刷新页面
                            $scope.getCatalogs($scope.clickCatalog.id);
                            $scope.rightDiv = false;
                        } else {
                            showOkModal("提示", data.message);
                        }
                    }
                });
            });
        }
    };

    //文件的右击
    $scope.rightClickTwos = function (index) {
        $scope.clickResource = true;
        var e = event || window.event;
        var scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
        var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
        var x = e.pageX || e.clientX + scrollX;
        var y = e.pageY || e.clientY + scrollY;
        $scope.rightDiv=true;
        $scope.canDown = true;
        $scope.where={
            "left":x-250,
            "top":y-159,
        };
        // 文件的重命名
        $scope.changeCatalog = function () {
            $scope.canEditR = true;//只有重命名的时候文件名的框才允许编辑
            $scope.rightDiv = false;
            $scope.renamingResource = $scope.resources[index];
            $timeout(function () {
                var element = document.getElementById("inputNames"+$scope.resources[index].id);
                if (element){
                    element.focus();
                }
            });
        };

        // 删除前校验
        $scope.preRemoveResource = function () {
            var resource = $scope.resources[index];

            //只有上传者和资源管理者能删除文件
            if ($scope.canRemoveResource || resource.staffId === $scope.staffId) {
                $scope.removeResource();
            } else {
                showOkModal("提示", "您没有删除此文件的权限!");
                return;
            }
        };

        //删除文件信息
        $scope.removeResource = function () {
            var resourceId = $scope.resources[index].id;
            $http.delete("rest/resource/" + resourceId +"/delete").success(function (data) {
                if (data !== null && data !== undefined) {
                    if(data.resultCode === "RESULT_SUCCESS") {
                        //刷新页面
                        $scope.getResources();
                    } else {
                        showOkModal("提示", data.message);
                    }
                }
            });
        };

        //下载前校验
        $scope.preDownResource = function () {
            var resourceId = $scope.resources[index].id;
            //先到后台判断有没有下载的权限
            $http.get("rest/resource/" + resourceId +"/preDown").success(function (data) {
                if (data !== null && data !== undefined) {
                    if(data.resultCode === "RESULT_SUCCESS") {
                        $scope.downResource(resourceId);
                    } else {
                        showConfirmModal(data.message + "是否发起此文件的下载申请？", function () {
                            //发起下载申请
                            $scope.downRequest = {};
                            $scope.downRequest.resourceId = resourceId;
                            $scope.downRequest.userId = $scope.staffId;
                            $scope.saveAndSubmitDown();
                        });
                    }
                }
            });
        };

        //修改文件资源信息
        $scope.preUpdateResource = function () {
            $scope.$emit('updateResource', $scope.resources[index]);
            angular.element("#uploadResourceModal").modal({show: true});
        };

        //打开移动文件资源模态框
        $scope.preMoveResource = function () {
            $scope.movingCataId = null;
            zTreeInit();
            angular.element("#resourceMoveModal").modal({show: true});
        };

        //提交移动资源
        $scope.submitMove = function () {
            var originResId = $scope.resources[index].id;   //原文件id
            var targetCataId = $scope.movingCataId;         //目标文件夹Id
            if (targetCataId !== null && targetCataId !== undefined) {
                $http.post("rest/resource/" + originResId +"/" + targetCataId + "/move").success(function (data) {
                    if (data !== null && data !== undefined) {
                        showOkModal("提示", data.message);
                        if(data.resultCode === "RESULT_SUCCESS") {
                            angular.element("#resourceMoveModal").modal('hide');
                            //刷新页面
                            $scope.getResources();
                        }
                    }
                });
            } else {
                showOkModal("提示", "请选择目标目录!");
            }
        };

    };

    $scope.saveAndSubmitDown = function () {
        $http.post("rest/down/save", $scope.downRequest).success(function (data) {
            if (data !== null && data !== undefined) {
                $scope.downRequests=data.otherData[0];
                $scope.$emit('downRequests', $scope.downRequests);
                if(data.resultCode === "RESULT_SUCCESS") {
                    var staffId = "undefined";
                    if(data.data.length <= 1){
                        if(data.data.length === 1){
                            staffId = data.data[0].staffId;
                            var applyName = data.data[0].name;
                            if (data.otherData) {
                                //下载信息的id
                                $scope.downRequest.id = data.otherData[0];
                            }
                        }
                        $http.post("rest/down/"+ staffId +"/submit", $scope.downRequest).success(function (data) {
                            if (data !== null && data !== undefined) {
                                if(data.resultCode === "RESULT_SUCCESS") {
                                    showOkModal('提示', "提交成功，下一审批人为&nbsp;&nbsp;&nbsp;" + applyName);
                                } else {
                                    showOkModal('提示', data.message);
                                }
                            }
                        });
                    } else if (data.data.length > 1) {
                        $scope.formData.processors = data.data;
                        var chooseId = $scope.formData.processor;
                        if(chooseId==undefined){
                            $scope.disSub=true;
                        }
                        $("#DownChooseModal").modal({show: true, keyboard: false, backdrop: 'static'});
                    }
                } else {
                    showOkModal('提示', data.message);
                }
            }
        }).error(function (data) {
            $("#addAndUpdateDownModal").modal({show: true});
        });
    };

    //文件的 重命名
    $scope.renameResource = function () {
        $scope.canEditR = false;
        var newName = document.getElementById("inputNames"+$scope.renamingResource.id).value;
        if (newName === "" || newName === null) {
            showOkModal("提示", "文件名非法!");
            //刷新页面
            $scope.getResources();
            return;
        }
        $scope.renamingResource.name = newName;
        $http.put("rest/resource/update", $scope.renamingResource).success(function (data) {
            if (data !== null && data !== undefined) {
                if(data.resultCode === "RESULT_SUCCESS") {
                    //刷新页面
                    $scope.getResources();
                } else {
                    showOkModal('提示', data.message);
                }
            }
        });
    };

    //第二层文件夹 重命名
    $scope.rename2 = function () {
        $scope.childcanEditC = false;
        var newVal = document.getElementById("inputName"+$scope.renameCatalog.id).value;
        if (newVal === "" || newVal === null) {
            showOkModal("提示", "文件名非法!");
            //刷新页面
            $scope.getNowResources(-1);
            return;
        }
        $scope.renameCatalog.name = newVal;
        $http.put("rest/catalog/update", $scope.renameCatalog).success(function (data) {
            if (data !== null && data !== undefined) {
                if(data.resultCode === "RESULT_SUCCESS") {
                    //刷新页面
                    $scope.getNowResources(-1);
                } else {
                    showOkModal('提示', data.message);
                }
            }
        });
    };

    //下载资源
    $scope.downResource = function (resourceId) {
        $http.post("rest/resource/"+resourceId+"/down", {}, {responseType: 'blob'}).success(function (data, status, headers) {
            var blob = new Blob([data], {type: "application/octet-stream"});
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

}]);