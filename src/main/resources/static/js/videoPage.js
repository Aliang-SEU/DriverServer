layui.use('table', function(){
    var table = layui.table;
    //第一个实例
    table.render({
        elem: '#demo'
        ,height: 600
        ,url: '/demo/table/user/' //数据接口
        ,page: true //开启分页
        ,cols: [//必须换行写
            [{field: 'id', title: 'ID', width:80, sort: false, fixed: 'left'}
                ,{field: 'filename', title: '文件名', width:200}
                ,{field: 'time', title: '上传时间', width:200, sort: true}
            ]]
    });
});