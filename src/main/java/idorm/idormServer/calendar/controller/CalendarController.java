//package idorm.idormServer.calendar.controller;
//
//import idorm.idormServer.calendar.domain.Calendar;
//import idorm.idormServer.calendar.dto.CalendarRequest;
//import idorm.idormServer.calendar.dto.DateFilterDto;
//import idorm.idormServer.calendar.dto.PageableDto;
//import idorm.idormServer.calendar.service.CalendarService;
//import idorm.idormServer.member.domain.Member;
//import idorm.idormServer.photo.service.PhotoService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequestMapping("calendar")
//@RequiredArgsConstructor
//@Api(tags = "캘린더")
//public class CalendarController {
//    // TODO : Authentication 추가
//
//    private final CalendarService calendarService;
//    private final PhotoService photoService;
//
//    @PostMapping("")
//    @ApiOperation(value = "Calendar 일정 저장", notes = "startTime, endTime 예시 \"2022-10-27T13:44:05\"")
//    Calendar save(@RequestBody CalendarRequest request) {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Member member = (Member)authentication.getPrincipal();
//
//        return calendarService.save(request.toEntity(null, member.getId()));
//    }
//
//    @GetMapping("{id}")
//    @ApiOperation("Calendar 일정 단건 조회")
//    Calendar find(@PathVariable Long id) {
//        return calendarService.find(id);
//    }
//
//    @GetMapping("list")
//    @ApiOperation(value = "Calendar 일정 목록 조회", notes = "날짜 필터 예시 \"2022-10-30\"")
//    Page<Calendar> searchList(PageableDto pageableDto, DateFilterDto dateFilterDto) {
//
//        return calendarService.searchList(pageableDto.toPageable(), dateFilterDto);
//    }
//
//    @PutMapping("{id}")
//    @ApiOperation("Calendar 일정 단건 수정")
//    Calendar update(@PathVariable Long id, @RequestBody CalendarRequest request) {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Member member = (Member)authentication.getPrincipal();
//
//        return calendarService.update(request.toEntity(id, member.getId()));
//    }
//
//    @DeleteMapping("{id}")
//    @ApiOperation("Calendar 일정 단건 삭제")
//    void delete(@PathVariable Long id) {
//        calendarService.delete(id);
//    }
//
//    @PostMapping("image")
//    @ApiOperation("Calendar 사진 업로드")
//    String uploadPicture(@RequestPart("file") MultipartFile file) {
//        return photoService.putImage(file);
//    }
//}
