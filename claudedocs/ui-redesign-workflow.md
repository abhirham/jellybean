# Jellyfin Android TV - UI Redesign Implementation Workflow
**Netflix-Style UI with Jetpack Compose for TV**

## Executive Summary

### Objective
Redesign Jellyfin Android TV to use a modern Netflix-style interface built entirely with Jetpack Compose for TV, replacing the current AndroidX Leanback-based UI.

### Target Design
- **Hero Banner**: Large featured content carousel at top with auto-rotation
- **Horizontal Media Carousels**: Multiple rows of content (Continue Watching, Trending, Genres, etc.)
- **Persistent Left Navigation**: Always-visible category/genre navigation pane
- **TV-Optimized**: Remote-friendly focus management and navigation

### Strategic Value
- **Modern UX**: Netflix-familiar interface improves user experience
- **Maintainability**: Declarative Compose code is easier to maintain than imperative Views
- **Performance**: Lazy composition and recomposition enable better performance
- **Future-Proof**: Compose is Android's modern UI toolkit with active development

---

## Current State Analysis

### Existing Architecture
```
UI Layer (Mixed):
├── AndroidX Leanback (Java/Kotlin)
│   ├── BrowseFragment/BrowseSupportFragment
│   ├── DetailsFragment
│   ├── SearchFragment
│   └── PlaybackTransportControlGlue
├── Traditional Views (Java/Kotlin)
│   ├── Custom views in ui/
│   └── XML layouts
└── Jetpack Compose (Limited)
    └── Some composables in ui/composable/

Data Layer: ✓ Modern (Repositories, ViewModels, Coroutines)
DI Layer: ✓ Koin (Compatible with Compose)
Playback Layer: ✓ Media3/ExoPlayer (Can integrate with Compose)
```

### Migration Challenges
1. **Large Surface Area**: 19 UI subdirectories with mixed Java/Kotlin code
2. **Leanback Dependency**: Heavy use of TV-specific Leanback components
3. **Focus Management**: Need to replicate TV D-pad navigation behavior
4. **Playback UI**: Critical video player controls require careful migration
5. **Testing**: New UI patterns require comprehensive testing strategy

---

## Target Architecture

### Compose for TV Stack
```
Jetpack Compose for TV (androidx.tv.*)
├── Navigation: Compose Navigation with TV extensions
├── Theme: TvMaterialTheme (NOT mobile Material)
├── Layouts:
│   ├── TvLazyRow (horizontal carousels)
│   ├── TvLazyColumn (vertical scrolling)
│   └── Custom TV-optimized layouts
├── Components:
│   ├── Card components (media items)
│   ├── Hero banner component
│   ├── Navigation drawer (persistent left pane)
│   └── Playback controls
└── Focus System: TV-specific focus management
```

### Key Dependencies
```kotlin
// Compose for TV (REQUIRED - use TV version, not mobile)
implementation("androidx.tv:tv-material:1.0.0")
implementation("androidx.tv:tv-foundation:1.0.0")

// Compose BOM (for version management)
implementation(platform("androidx.compose:compose-bom:2025.10.01"))

// Compose essentials
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.foundation:foundation")
implementation("androidx.compose.runtime:runtime")

// Compose integration
implementation("androidx.activity:activity-compose:1.11.0")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
implementation("androidx.navigation:navigation-compose")

// Existing dependencies (keep)
implementation(libs.koin.androidx.compose) // Already in project
implementation(libs.coil.compose) // Already in project
```

---

## Implementation Phases

### Phase 1: Foundation Setup (Week 1-2)
**Goal**: Establish Compose for TV infrastructure

#### Tasks
- [ ] **1.1 Dependencies** (1 day)
  - Add Compose for TV dependencies to `app/build.gradle.kts`
  - Update Compose BOM to latest version
  - Add TV-specific testing dependencies
  - Verify no conflicts with existing Compose usage

- [ ] **1.2 Theme System** (2-3 days)
  - Create `TvTheme.kt` with TvMaterialTheme configuration
  - Define TV-optimized color schemes (dark mode primary)
  - Set up typography scale for TV viewing distances
  - Configure shapes and dimensions for TV screens
  - Create theme preview composables for testing

- [ ] **1.3 Navigation Architecture** (3-4 days)
  - Design navigation graph structure (NavGraph.kt)
  - Implement navigation host with TV extensions
  - Create navigation destinations enum/sealed class
  - Set up deep linking support
  - Design back stack management for TV

- [ ] **1.4 Focus Management System** (2-3 days)
  - Create `FocusManager.kt` for global focus coordination
  - Implement focus restoration on navigation
  - Set up D-pad input handling utilities
  - Create focus debugging tools
  - Document focus patterns and best practices

- [ ] **1.5 Base Composables** (2 days)
  - Create `Screen.kt` base composable wrapper
  - Implement loading states composable
  - Create error state composable
  - Build empty state composable
  - Set up composable preview utilities

**Deliverables**:
- Working Compose for TV setup
- Theme system with preview app
- Navigation framework
- Focus management foundation
- Base composable library

**Dependencies**: None (start immediately)

**Risks**:
- Version conflicts with existing Compose → Mitigation: Test thoroughly, use BOM
- Team learning curve → Mitigation: Documentation, pair programming

---

### Phase 2: Core Component Library (Week 3-5)
**Goal**: Build reusable Netflix-style UI components

#### Parallel Workstreams (can run simultaneously)

#### 2.A Hero Banner Component (5-7 days)
- [ ] **2.A.1 Design Specification**
  - Define hero banner layout (16:9 aspect ratio)
  - Specify animation timings (crossfade, auto-rotate)
  - Design overlay gradient and text layout
  - Plan focus behavior and user interaction

- [ ] **2.A.2 Implementation**
  - Create `HeroBanner.kt` composable
  - Implement Pager for swipeable hero items
  - Add auto-rotation timer (15-20 seconds)
  - Build overlay with title, description, metadata
  - Add CTA buttons (Play, More Info)
  - Implement focus indicators

- [ ] **2.A.3 Integration**
  - Connect to data layer (featured content repository)
  - Handle loading and error states
  - Add image loading with Coil
  - Implement backdrop blur effects
  - Add progress indicators for auto-rotation

#### 2.B Horizontal Carousel Component (5-7 days)
- [ ] **2.B.1 Design Specification**
  - Define carousel item sizing and spacing
  - Specify focus scaling animations
  - Design row header layout (title, "See All" button)
  - Plan infinite scroll vs paginated loading

- [ ] **2.B.2 Implementation**
  - Create `MediaCarousel.kt` with TvLazyRow
  - Implement `CarouselItem.kt` card component
  - Add focus scaling and shadow effects
  - Build carousel header with navigation
  - Implement lazy loading and pagination
  - Add horizontal scroll indicators

- [ ] **2.B.3 Variants**
  - Create portrait card variant (posters)
  - Create landscape card variant (backdrops)
  - Create circular card variant (avatars/channels)
  - Implement "Continue Watching" variant with progress bar
  - Build "Live TV" variant with channel info

#### 2.C Persistent Navigation Drawer (4-6 days)
- [ ] **2.C.1 Design Specification**
  - Define drawer width (15-20% of screen)
  - Specify navigation item layout
  - Design focus behavior and selection states
  - Plan drawer show/hide interaction (if needed)

- [ ] **2.C.2 Implementation**
  - Create `NavigationDrawer.kt` composable
  - Implement navigation item list with icons
  - Add selected state indicators
  - Build focus management for drawer
  - Implement smooth expand/collapse (optional)
  - Add user profile section at top

- [ ] **2.C.3 Navigation Items**
  - Home
  - My Library / Continue Watching
  - Movies
  - TV Shows
  - Live TV & Guide
  - Search
  - Settings
  - User switching

**Deliverables**:
- Hero banner component with auto-rotation
- Horizontal carousel component (multiple variants)
- Persistent navigation drawer
- Component documentation and preview app

**Dependencies**: Phase 1 complete (theme, navigation, focus system)

**Risks**:
- Performance with large carousels → Mitigation: Use TvLazyRow, proper keys, profiling
- Focus complexity with overlapping elements → Mitigation: Explicit focus ordering

---

### Phase 3: Screen Implementation (Week 6-10)
**Goal**: Migrate key screens to new Compose UI

#### Priority Order (sequential with some parallelization)

#### 3.1 Home Screen (Week 6-7)
**Priority**: CRITICAL (highest visibility)

- [ ] **3.1.1 Layout Structure**
  - Create `HomeScreen.kt` composable
  - Implement main layout with navigation drawer
  - Add hero banner at top
  - Stack multiple carousels below hero
  - Implement vertical scrolling container

- [ ] **3.1.2 Content Integration**
  - Connect to existing HomeViewModel
  - Load featured content for hero banner
  - Load carousel data (Continue Watching, Trending, etc.)
  - Implement pull-to-refresh
  - Handle empty/error states

- [ ] **3.1.3 Navigation Integration**
  - Wire up navigation drawer selection
  - Implement carousel item click handlers
  - Add hero banner CTA navigation
  - Handle deep links to home screen

- [ ] **3.1.4 Testing & Polish**
  - Focus testing with remote control
  - Performance profiling
  - Animation timing adjustments
  - Accessibility testing

**Success Criteria**: Fully functional home screen replacing current BrowseFragment

#### 3.2 Detail Screen (Week 7-8)
**Priority**: HIGH (users click from home)

- [ ] **3.2.1 Implementation**
  - Create `DetailScreen.kt` composable
  - Implement backdrop with gradient overlay
  - Add media info section (title, year, rating, etc.)
  - Build action buttons row (Play, Add to Library, etc.)
  - Add "More Like This" carousel
  - Include cast & crew information
  - Show episodes list (for TV shows)

- [ ] **3.2.2 Integration**
  - Connect to detail ViewModel
  - Implement Play button → playback transition
  - Handle seasons/episodes navigation
  - Add trailer playback support

#### 3.3 Library/Browse Screens (Week 8-9)
**Priority**: HIGH (frequent use)

- [ ] **3.3.1 Library Screen**
  - Create grid layout with filters
  - Implement sorting options
  - Add genre filtering
  - Handle large collections efficiently

- [ ] **3.3.2 Genre/Category Screens**
  - Create category browse screens
  - Implement filtering and sorting
  - Add alphabet picker for navigation

#### 3.4 Search Screen (Week 9)
**Priority**: MEDIUM

- [ ] **3.4.1 Implementation**
  - Create `SearchScreen.kt` with keyboard
  - Implement search suggestions
  - Show results in carousel/grid
  - Add voice search support (if available)

#### 3.5 Settings Screen (Week 10)
**Priority**: MEDIUM-LOW

- [ ] **3.5.1 Implementation**
  - Migrate preference screens to Compose
  - Use TV-optimized preference components
  - Maintain existing settings functionality
  - Add visual settings previews where applicable

**Deliverables**:
- Complete home screen (Compose)
- Detail screen (Compose)
- Library/browse screens (Compose)
- Search screen (Compose)
- Settings screens (Compose)

**Dependencies**: Phase 2 components complete

**Migration Strategy**:
- Create new Compose screens alongside legacy screens
- Use feature flag to toggle between old/new UI
- Gradual rollout (internal testing → beta → production)
- Keep legacy screens as fallback for 2-3 releases

---

### Phase 4: Playback Integration (Week 11-12)
**Goal**: Integrate video player with new Compose UI

#### Tasks
- [ ] **4.1 Playback UI Analysis** (2 days)
  - Analyze current PlaybackOverlayFragment
  - Identify required controls and features
  - Design Compose playback UI layout

- [ ] **4.2 Player Controls** (5-7 days)
  - Create `PlaybackControlsOverlay.kt`
  - Implement timeline scrubbing
  - Add play/pause, skip, subtitle controls
  - Build quality/audio track selection
  - Implement chapter navigation
  - Add PiP support

- [ ] **4.3 Integration** (3-4 days)
  - Connect to existing MediaManager
  - Integrate with Media3 player
  - Handle playback state changes
  - Implement video surface layout
  - Add gesture controls (optional)

- [ ] **4.4 Testing** (2-3 days)
  - Test with various media types
  - Verify subtitle rendering
  - Check audio sync
  - Performance testing
  - Focus behavior testing

**Deliverables**:
- Fully functional Compose-based video player
- Feature parity with existing player
- Smooth integration with new UI

**Dependencies**: Phase 3 screens (detail screen click → playback)

**Risks**:
- Critical functionality - must work perfectly → Mitigation: Extensive testing, phased rollout
- Performance concerns → Mitigation: Profile early, optimize rendering

---

### Phase 5: Polish & Optimization (Week 13-14)
**Goal**: Refinement, performance, and quality

#### Tasks
- [ ] **5.1 Performance Optimization**
  - Profile with Android Profiler
  - Optimize recomposition (remember(), derivedStateOf())
  - Reduce overdraw
  - Optimize image loading
  - Implement list prefetching
  - Test on low-end devices

- [ ] **5.2 Animations & Transitions**
  - Polish screen transitions
  - Refine focus animations
  - Add loading animations
  - Implement shared element transitions
  - Smooth carousel scrolling

- [ ] **5.3 Accessibility**
  - Ensure proper focus order
  - Add content descriptions
  - Test with TalkBack
  - Verify remote control compatibility
  - Test on various TV brands (Samsung, LG, Sony, etc.)

- [ ] **5.4 Edge Cases**
  - Handle orientation changes (if applicable)
  - Test with different aspect ratios
  - Verify behavior with slow networks
  - Handle empty states gracefully
  - Test error recovery

- [ ] **5.5 Documentation**
  - Update CLAUDE.md with Compose patterns
  - Document component usage
  - Create migration guide for contributors
  - Add architecture diagrams

**Deliverables**:
- Optimized performance across device range
- Polished animations and transitions
- Comprehensive accessibility support
- Complete documentation

**Dependencies**: All previous phases

---

## Dependency Map

```
Phase 1: Foundation Setup
    ↓
    ├─────────┬─────────┬─────────→ Phase 2: Component Library
    │         │         │             ├── 2.A Hero Banner
    │         │         │             ├── 2.B Carousels (parallel)
    │         │         │             └── 2.C Navigation (parallel)
    │         │         │                 ↓
    │         │         └─────────→ Phase 3: Screen Implementation
    │         │                       ├── 3.1 Home Screen (sequential)
    │         │                       ├── 3.2 Detail Screen
    │         │                       ├── 3.3 Library Screens (parallel)
    │         │                       ├── 3.4 Search Screen (parallel)
    │         │                       └── 3.5 Settings (parallel)
    │         │                           ↓
    │         └─────────────────────→ Phase 4: Playback Integration
    │                                     ↓
    └─────────────────────────────→ Phase 5: Polish & Optimization
```

**Critical Path**: Phase 1 → Phase 2 → Phase 3.1 (Home) → Phase 3.2 (Detail) → Phase 4 (Playback) → Phase 5

**Parallelization Opportunities**:
- Phase 2: All three components can be built in parallel
- Phase 3: After home screen, other screens can proceed in parallel
- Significant time savings with proper team coordination

---

## Testing Strategy

### Unit Testing
- **Component Tests**: Snapshot tests for each Compose component
- **ViewModel Tests**: Existing tests should continue to work (data layer unchanged)
- **Navigation Tests**: Verify navigation graph routes

### UI Testing
- **Compose Testing**: Use `@Composable` preview and testing utilities
- **Focus Testing**: Comprehensive D-pad navigation testing
  - Tab through all focusable elements
  - Verify focus indicators
  - Test circular focus behavior
  - Verify focus restoration after navigation

### Integration Testing
- **End-to-End**: User journeys from home → detail → playback
- **Data Loading**: Test with real API responses
- **Error Scenarios**: Network failures, empty states, invalid data

### Device Testing Matrix
| Device Type | Priority | Target Devices |
|-------------|----------|----------------|
| Android TV | HIGH | Shield TV, Chromecast with Google TV |
| Fire TV | HIGH | Fire TV Stick 4K, Fire TV Cube |
| Samsung TV | MEDIUM | 2023+ models with Tizen/Android |
| Low-end devices | MEDIUM | Older streaming sticks (2-3 years) |

### Performance Testing
- **Metrics to Track**:
  - Frame rate (target: 60fps)
  - Recomposition counts (minimize)
  - Memory usage
  - Image loading time
  - Screen transition time
  - Time to interactive (TTI)

- **Tools**:
  - Android Studio Profiler
  - Compose Layout Inspector
  - `@Composable` preview performance
  - Firebase Performance Monitoring

---

## Migration Strategy

### Gradual Rollout Approach

#### Step 1: Feature Flag (Week 6)
```kotlin
// In Koin module or shared preferences
object FeatureFlags {
    var useNewComposeUI: Boolean = false
}

// In navigation/routing
if (FeatureFlags.useNewComposeUI) {
    // Route to Compose screens
} else {
    // Route to legacy screens
}
```

#### Step 2: Internal Testing (Week 7-8)
- Enable for development builds only
- Internal team testing
- Gather feedback and iterate

#### Step 3: Beta Testing (Week 9-11)
- Enable for beta channel users (opt-in)
- Monitor crash reports and feedback
- Fix critical issues

#### Step 4: Phased Production Rollout (Week 12-14)
- 10% of users → monitor metrics
- 50% of users → verify stability
- 100% rollout → full migration
- Keep legacy code for 2-3 releases (emergency rollback)

#### Step 5: Legacy Cleanup (Week 15+)
- Remove feature flags
- Delete legacy UI code
- Update documentation

### Coexistence Strategy
During migration, both UIs will coexist:
- **Data Layer**: Shared (ViewModels, Repositories) - no duplication
- **Navigation**: Conditional routing based on feature flag
- **Dependencies**: Both Leanback and Compose for TV temporarily
- **Testing**: Test both code paths during transition

---

## Resource Requirements

### Team Composition
- **Android Developers**: 2-3 developers with Kotlin experience
  - At least 1 with Compose experience (lead)
  - All should learn Compose for TV patterns
- **UI/UX Designer**: 1 designer for Netflix-style mockups and specifications
- **QA Engineer**: 1 tester for focus, accessibility, device testing
- **Optional**: TV device farm access or partnerships for testing

### Learning Resources
- [Jetpack Compose for TV Documentation](https://developer.android.com/training/tv/playback/compose)
- [TV Material Design Guidelines](https://m3.material.io/foundations/layout/applying-layout/tv)
- [JetStream Sample App](https://github.com/android/compose-samples/tree/main/JetStream) - Netflix-style reference
- [TV Material Catalog](https://github.com/androidx/media/tree/release/demos/catalog) - Component examples

### External Dependencies
- **Design Assets**: High-quality hero banners, promotional art
- **API Support**: Ensure Jellyfin API provides featured content, carousels
- **Testing Devices**: Access to range of TV devices (rent, borrow, or purchase)

---

## Timeline & Estimates

### Optimistic Timeline (14 weeks, 3.5 months)
| Phase | Duration | Team Size | Effort |
|-------|----------|-----------|--------|
| Phase 1: Foundation | 2 weeks | 2 devs | 4 dev-weeks |
| Phase 2: Components | 3 weeks | 3 devs (parallel) | 9 dev-weeks |
| Phase 3: Screens | 5 weeks | 2-3 devs | 12 dev-weeks |
| Phase 4: Playback | 2 weeks | 1-2 devs | 3 dev-weeks |
| Phase 5: Polish | 2 weeks | 2 devs | 4 dev-weeks |
| **Total** | **14 weeks** | **2-3 devs** | **32 dev-weeks** |

### Realistic Timeline (18-20 weeks, 4.5-5 months)
Add 30% buffer for:
- Learning curve
- Unexpected issues
- Review cycles
- Comprehensive testing
- Beta feedback iteration

### Conservative Timeline (24 weeks, 6 months)
If team is learning Compose or has other priorities, allow 6 months for comfortable delivery.

---

## Success Metrics

### User Experience Metrics
- **User Engagement**: Time spent in app (target: +15-20%)
- **Content Discovery**: Items clicked from carousels (target: +25%)
- **Navigation Efficiency**: Clicks to content (target: -20%)
- **User Satisfaction**: App store ratings (target: maintain or improve)

### Technical Metrics
- **Performance**: 60fps frame rate maintained (target: >95% of frames)
- **Crash Rate**: No increase from baseline (target: <1% crash rate)
- **Memory Usage**: Comparable to current app (target: ≤ +10%)
- **Load Times**: Screen transitions (target: <500ms)

### Development Metrics
- **Code Coverage**: Maintain or improve (target: >70% for new code)
- **Bug Density**: Issues per KLOC (target: <5 bugs per KLOC)
- **Maintainability**: Reduced code complexity (target: cyclomatic complexity <10)

---

## Risk Assessment

| Risk | Severity | Likelihood | Mitigation |
|------|----------|------------|------------|
| Performance degradation on low-end devices | HIGH | MEDIUM | Early profiling, TvLazy* components, testing on target devices |
| Focus management bugs | HIGH | HIGH | Comprehensive testing, focus debugging tools, user testing |
| Breaking existing functionality | HIGH | MEDIUM | Feature flags, gradual rollout, extensive testing, legacy fallback |
| Team learning curve delays | MEDIUM | HIGH | Training, documentation, pair programming, start with pilot screen |
| Design inconsistencies | MEDIUM | MEDIUM | Design system, component library, regular design reviews |
| Playback integration issues | HIGH | MEDIUM | Tackle last, extensive testing, maintain fallback |
| API limitations for Netflix-style features | MEDIUM | LOW | Work with Jellyfin backend team, mock data for testing |
| Device fragmentation issues | MEDIUM | MEDIUM | Device testing matrix, responsive layouts, fallback behaviors |

---

## Next Steps

### Immediate Actions (Pre-Phase 1)
1. **Stakeholder Alignment**
   - Review this workflow with team leads
   - Get design team to create Netflix-style mockups
   - Align on timeline and resource allocation

2. **Proof of Concept**
   - Build simple hero banner prototype
   - Test Compose for TV on target devices
   - Validate performance assumptions
   - Demo to stakeholders for buy-in

3. **Team Preparation**
   - Compose training for team members
   - Set up TV testing devices
   - Review JetStream sample app
   - Create development branch

4. **Project Setup**
   - Create feature tracking board (Jira/GitHub Projects)
   - Set up CI/CD for Compose UI testing
   - Configure feature flags infrastructure
   - Plan sprint structure

### Phase 1 Kickoff Checklist
- [ ] Team trained on Compose for TV basics
- [ ] Design mockups approved
- [ ] Testing devices procured
- [ ] Feature branch created
- [ ] CI/CD configured
- [ ] Stakeholder approval obtained

---

## Appendices

### A. Compose for TV Component Mapping

| Current Component | New Compose Component | Notes |
|-------------------|----------------------|-------|
| BrowseFragment | HomeScreen composable | Complete redesign |
| DetailsFragment | DetailScreen composable | Add hero layout |
| SearchFragment | SearchScreen composable | New keyboard UI |
| ItemRowView | MediaCarousel composable | TvLazyRow based |
| PlaybackOverlay | PlaybackControlsOverlay | Compose overlay |
| PreferenceScreen | Compose preferences | androidx.compose.material3 |

### B. Key Compose for TV APIs

```kotlin
// Lazy lists for carousels
TvLazyRow { items(data) { MediaCard(it) } }
TvLazyColumn { items(data) { Row(it) } }

// Focus management
Modifier.focusable()
Modifier.onFocusChanged { }
FocusRequester()

// TV-specific interactions
Modifier.handleDPadKeyEvents { }

// Theme
TvMaterialTheme(colorScheme, typography, shapes) { }

// Navigation
val navController = rememberNavController()
NavHost(navController) { composable("home") { } }
```

### C. Reference Implementations
- **JetStream**: https://github.com/android/compose-samples/tree/main/JetStream
- **TV Material Catalog**: https://github.com/androidx/media/tree/release/demos/catalog
- **Compose TV Samples**: https://developer.android.com/develop/ui/compose/layouts/tv

---

**Document Version**: 1.0
**Last Updated**: 2025-11-03
**Owner**: Jellyfin Android TV Team
**Status**: Draft - Pending Approval
