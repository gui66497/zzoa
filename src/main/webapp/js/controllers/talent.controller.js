'use strict';

indexModule.controller('talentController', ['$scope', '$http', '$q','NgTableParams','$timeout',function ($scope, $http, $q, NgTableParams, $timeout) {
    moment.locale('zh-cn');     // Simplified chinese

    var self = this;
    $scope.tableData = {};
    $scope.req = {};
    $scope.section = {};

    function initialRequest() {
        $scope.req.paging = {};
        $scope.req.paging.pageNo = 1;
        $scope.req.paging.pageSize = 10;
    }
    //form表单
    $scope.formData = {};

    self.tableParams = new NgTableParams({'count': 10}, {
        getData: function (params) {
            //清空请求
            initialRequest();
            console.log("params的url内容", params.url());
            var allParams = params.url();

            $scope.req.paging.pageNo = allParams.page;
            $scope.req.paging.pageSize = allParams.count;

            console.log("req的值为：", $scope.req);

            var promise = $http.post("rest/talent/page", $scope.req);
            return promise.then(function (resut) {
                var response = resut.data;
                var total = response.otherData[0];
                params.total(total);
                $scope.tableData = response.data;
                return response.data;

            });
        }
    });

    //多选框默认文本替换
    $scope.localLang = {
        nothingSelected : "请选择人才类别"
    };

    function loadResumeType(){
        $http.get("rest/dic/RESUME_TYPE/getDicInfo", null).success(function (data) {
            if (data != null && data != undefined) {
                $scope.typeSections = data.data;
                console.log("typeSections", data);
                $scope.typeSearchers = angular.copy(data.data);
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    }
    loadResumeType();

    //添加人才信息
    $scope.addTalent = function () {
        document.getElementById("phone").value="";
        $scope.p="";
        $scope.c="";
        loadResumeType();
        $scope.formData = {};
        $scope.formData.sex = "1";
        document.getElementById("userdefinedFile").value = '';
        $scope.dis = false;
        $scope.fileUp = true;
        angular.element("#addAndUpdateTalent").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    //文件框onchange事件
    $scope.fileNameChanged = function (ele) {
        console.log(ele.value);
        console.log($scope.formData.resumeName);
        var filePath = ele.value;
        var pos = filePath.lastIndexOf("\\") + 1;
        var fileName = filePath.substr(pos);
        $scope.formData.resumeName = fileName;
        document.getElementById("userdefinedFile").value = fileName;
    };

    $scope.submit = function () {
        var types = [];
        angular.forEach($scope.selectedTypes, function (type) {
            types.push(type.dicValue);
        });
        if (types.length < 1) {
            showOkModal("提示", "请选择人才类别!");
            return;
        }
        $scope.formData.type = types.join(",");
        if($scope.formData.id!=null){
            $http.post("rest/talent/update", $scope.formData).success(function (data) {
                if (data != null && data != undefined) {
                    if (data.resultCode == "RESULT_SUCCESS") {
                        showOkModal('提示', "修改成功！");
                        self.tableParams.reload();
                        angular.element("#addAndUpdateTalent").modal('hide');
                    } else {
                        showOkModal('提示', data.message);
                    }
                }
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
        } else {
            var files = document.getElementsByName('file')[0].files;
            if (files.length < 1) {
                showOkModal("提示信息", "请先选择简历文件！");
                return;
            }
            $http.post("rest/talent/add", $scope.formData).success(function (data) {
                if (data != null && data != undefined) {
                    if (data.resultCode == "RESULT_SUCCESS") {
                        $scope.upload(data.message);
                    } else {
                        showOkModal('提示', data.message);
                    }
                }
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
        }
    };

    $scope.batchTalent=function () {
        loadResumeType();
        $scope.formData = {};
        $(".liFile").remove();
        document.getElementById("batchFile").value='';
        document.getElementById("userdefinedFileTwo").value="";
        angular.element("#batchTalent").modal({show: true, keyboard: false, backdrop: 'static'});
    };
    var oFile = document.getElementById('batchFile');
    var oList = document.getElementById('fileList');
    var fileList;

    oFile.onchange = function(e){
        var filepath=document.getElementById('batchFile').value;
        filepath=filepath.substring(filepath.lastIndexOf('.')+1,filepath.length);
        if(filepath == 'html'||filepath == 'mht'||filepath == 'zip'){
            $(".liFile").remove();
            if(this.files.length>0){
                fileList = Array.prototype.slice.call(this.files);
                for(var i=0;i<this.files.length;i++){
                    var node = document.createElement('li');
                    node.setAttribute('id','li_'+i);
                    node.setAttribute('class','liFile');
                    var html =this.files[i].name;
                    node.innerHTML = html;
                    oList.appendChild(node);
                    document.getElementById("userdefinedFileTwo").value=this.files.length+"个文件";
                }
            }
        }else if(filepath != 'html'&& filepath != 'mht'&& filepath != 'zip'){
            showOkModal("提示", "只能上传HTML格式或mht格式的文件或者zip压缩文件");
            $(".liFile").remove();
            document.getElementById("userdefinedFileTwo").value="";
            document.getElementById("batchFile").value='';
        }
    };

    $scope.queryTalent=function () {
        if($scope.typesForSearch) {
            var types = [];
            angular.forEach($scope.typesForSearch, function (type) {
                types.push(type.dicKey);
            });
            $scope.req.type = types.join(",");
            self.tableParams._params.page = 1;
        }

        if($scope.req.type==""){
            $scope.req.type=null;
        }else if($scope.req.userName==''){
            $scope.req.userName=null
        }else if($scope.req.keyword==''){
            $scope.req.keyword=null
        }
        self.tableParams.reload();
        self.tableParams._params.page=1;
    };

    $scope.showTalentDetail=function (index) {
        $scope.dis=true;
        $scope.fileUp=false;
        var id = $scope.tableData[index].id;
        $http.get("rest/talent/"+id+"/query", {}).success(function (data) {
            if (data != null && data != undefined) {
                $scope.formData = data;
                angular.element("#addAndUpdateTalent").modal({show: true, keyboard: false, backdrop: 'static'});
            }
        }).error(function (data) {
            angular.element("#serverErrorModal").modal({show: true});
        });
    };

    //预览简历
    $scope.preview = function (index) {
        var id = $scope.tableData[index].id;
        var projectName = getProjectName();
        $http.get("rest/talent/" + id + "/preview" ).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode == "RESULT_SUCCESS") {
                    var fileName = data.obj;
                    $scope.previewFilename = fileName;
                    var postfix = fileName.substr(fileName.lastIndexOf(".") + 1);
                    //如果是pdf文件则调用pdf.js处理  不是的话直接展示
                    if ("pdf" === postfix) {
                        //newWin.location = 'exts/pdfJs/web/viewer.html?file=' + prefix + "/" + fileName;
                        newWindow('exts/pdfJs/web/viewer.html?file=' + projectName + "/pdf/" + fileName);
                    } else if ("html" === postfix) {
                        window.open(projectName + "/resumeHtml/"+ fileName);
                    } else {
                        console.log("图片：" + fileName);
                        //转义路径 否则中文会乱码
                        //newWin.location = projectName + "/page/imgPreview.html?file=" + encodeURI(fileName);
                        newWindow(projectName + "/page/imgPreview.html?file=" + encodeURI(fileName));
                    }

                } else {
                    showOkModal("提示", data.message);
                }
            }
        }).error(function (data) {
            angular.element("#serverErrorModal").modal({show: true});
        });
    };

    /**
     * 通过模拟a标签打开新窗口
     * @param url
     * @param id
     */
    function newWindow(url, id) {
        var a = document.createElement('a');
        a.setAttribute('href', url);
        a.setAttribute('target', '_blank');
        a.setAttribute('id', id);
        if(!document.getElementById(id)) {
            document.body.appendChild(a);
        }
        a.click();
    }

    $scope.showEditTalent = function (index) {
        $scope.p=$scope.tableData[index].area;
        var all=document.getElementsByClassName("select2-selection__choice");
        if(all[0]!=null){
            $(document).ready(function(){
                $(".select2-selection__choice").remove();
            });
        }
        $scope.dis=false;
        $scope.fileUp=false;
        var id = $scope.tableData[index].id;
        $http.get("rest/talent/"+id+"/query", {}).success(function (data) {
            if (data != null && data != undefined) {
                $scope.formData = data;
                console.log("da", data);
                var types = data.type;
                if (types) {
                    angular.forEach($scope.typeSections, function (section) {
                        section.ticked = types.split(",").indexOf(section.dicValue) > -1;
                    });
                }
                angular.element("#addAndUpdateTalent").modal({show: true, keyboard: false, backdrop: 'static'});
            }
        }).error(function (data) {
            angular.element("#serverErrorModal").modal({show: true});
        });
    };

    $scope.showDeleteTalent = function (index) {
        var id = $scope.tableData[index].id;
        showConfirmModal("确定要删除吗?", function () {
            $http.delete("rest/talent/"+id+"/delete", {}).success(function (data) {
                if (data != null && data != undefined) {
                    if (data.resultCode == "RESULT_SUCCESS") {
                        showOkModal('提示', "删除成功！");
                        self.tableParams.reload();
                    } else {
                        showOkModal('提示', data.message);
                    }
                }
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
        });

    };

    /**
     * 上传
     */
    $scope.upload = function (fileName) {
        var files = document.getElementsByName('file')[0].files;
        if (files.length < 1) {
            showOkModal("提示信息", "请先选择文件！");
            return;
        }
        $http({
            method: 'POST',
            url: 'rest/talent/'+fileName+'/upload',
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
            angular.element("#addAndUpdateTalent").modal('hide');
        }).error(function (err, status) {
            console.log(err);
        });
    };

    /**
     * 批量上传
     */
    $scope.batchSubmit = function(){
        $scope.hideFrom=true;
        var files = document.getElementsByName('batchFile')[0].files;
        if (files.length < 1) {
            $scope.hideFrom=false;
            showOkModal("提示信息", "请先选择文件！");
            return;
        }
        var types = [];
        angular.forEach($scope.selectedTypes, function (type) {
            types.push(type.dicKey);
        });
        if (types.length < 1) {
            $scope.hideFrom=false;
            showOkModal("提示", "请选择人才类别!");
            return;
        }
        $scope.formData.type = types.join(",");
        $scope.uploadFile(files,files.length,0);
    };

    $scope.uploadFile = function (files,len,i){
        var type = $scope.formData.type.replace(/\//g,"&");
        if($scope.formData.keyword==''){
            $scope.formData.keyword=undefined;
        }
        if($scope.formData.note==''){
            $scope.formData.note=undefined;
        }

        $http({
            method: 'POST',
            url: 'rest/talent/'+$scope.formData.source+'/'+type+'/'+$scope.formData.keyword+'/'+$scope.formData.note+'/'+files[i].name.substring(files[i].name.lastIndexOf('.')+1,files[i].name.length)+'/batchUpload',
            headers: {
                'Content-Type': undefined
            },
            data: {
                file: files[i]
            },
            transformRequest: function (data) {
                var formData = new FormData();
                formData.append('file', data.file);
                return formData;
            }
        }).success(function (data) {
            if(i<len-1){
                $scope.hideFrom=false;
                $scope.uploadFile(files,len,i+1);
            }else {
                //请求成功
                $scope.hideFrom=false;
                showOkModal('提示', "添加成功！");
                self.tableParams.reload();
                angular.element("#batchTalent").modal('hide');
            }
        }).error(function (err, status) {
            $scope.hideFrom=false;
            console.log(err);
        });
    };

    $scope.downloadResume=function (index) {
        var id = $scope.tableData[index].id;
        var fileName = $scope.tableData[index].userName;
        $http.post("rest/talent/"+id+"/down", $scope.req, {responseType: 'blob'}).success(function (data, status, headers) {
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

    //获取项目名 如/zzoa
    function getProjectName() {
        //获取主机地址之后的目录，如： /zzoa/welcome.jsp
        var pathName = window.document.location.pathname;
        //获取带"/"的项目名，如：/zzoa
        return pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    }

    $scope.myKeyup = function (e) {
        var keycode = window.event?e.keyCode:e.which;
        if(keycode == 13){
            $scope.queryTalent();
        }
    };
}]);
