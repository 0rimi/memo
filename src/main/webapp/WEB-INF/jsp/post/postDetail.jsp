<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1>글상세/수정</h1>

		<input type="text" id="subject" class="form-control" value="${post.subject}" placeholder="제목을 입력해주세요">
		<textarea class="form-control" rows="10" id="content" placeholder="내용을 입력해주세요">${post.content}</textarea>
		
		<!-- 이미지가 있을 때만 이미지 영역 추가 -->
		<c:if test="${not empty post.imgPath}">
			<div>
				<img src="${post.imgPath}" alt="업로드 이미지" width="300px">
			</div>
		</c:if>
		
		<div class="d-flex justify-content-end my-3">
			<input type="file" id="file" accept=".jpg,.jpeg,.png,.gif">
		</div>
		

		<div class="d-flex justify-content-between">
			<button type="button" id="postDeleteBtn" data-post-id="${post.id}" class="btn btn-danger">삭제</button>
			<div>
				<a href="/post/post_list_view" type="button" id="postListBtn" class="btn btn-dark">목록으로</a>
				<button type="button" id="postUpdateBtn" class="btn btn-primary" data-post-id="${post.id}">수정하기</button>
			</div>
		</div>
	</div>
</div>

<!-- script -->
<script>
	$(document).ready(function() {
		//////////수정버튼/////////////
		$('#postUpdateBtn').on('click',function(){
			let subject = $('#subject').val().trim();
			if(subject == ''){
				alert('제목을 입력하세요');
				return;
			}
			let content = $('#content').val();
			console.log(content);
			
			let file = $('#file').val();
			console.log(file);	//C:\fakepath\Fmq0iFNacAANVUa.jpg
			
			//파일이 업로드 된 경우 확장자 체크
			if (file != '') {
				let ext = file.split(".").pop().toLowerCase();
				if ($.inArray(ext, ['jpg', 'jpeg', 'png', 'gif']) == -1) {
					alert("이미지 파일만 업로드 할 수 있습니다.");
					$('#file').val(""); // 파일을 비운다.
					return;
				}
			}
			
			//포스트번호
			let postId = $(this).data('post-id');
			console.log('포스트번호 : '+postId);
			
			let formData = new FormData();
			formData.append('postId', postId);
			formData.append('subject', subject);
			formData.append('content', content);
			formData.append('file',$('#file')[0].files[0]);
			
			//AJAX
			$.ajax({
				//rq
				type:"PUT"
				,url:"/post/update"
				,data:formData
				,enctype:"multipart/form-data"	//파일업로드를 위한 필수설정
				,processData:false
				,contentType:false
				//rs
				,success:function(data){
					if(data.code == 1){
						alert('수정되었습니다!');
						location.reload();
					}else{
						alert(data.errorMessage);
					}
				}
				,error:function(e){
					alert('메모 수정에 실패했습니다.')
				}
			});
		});
		////////////////////////////////////
		
		////////////////글삭제////////////////
		$('#postDeleteBtn').on('click',function(){
			let postId = $(this).data('post-id');
			console.log('삭제포스트 :',postId);
			
			$.ajax({
				//rq
				type:"DELETE"
				,url:"/post/delete"
				,data:{"postId":postId}
				//rs
				,success:function(data){
					if(data.result == '성공'){
						alert('삭제되었습니다.');
						location.href="/post/post_list_view";
					}else{
						alert(data.errorMessage);
					}
				},error:function(e){
					console.log('메모삭제실패');
				}
			});
		});
		////////////////글삭제////////////////
	});
</script>